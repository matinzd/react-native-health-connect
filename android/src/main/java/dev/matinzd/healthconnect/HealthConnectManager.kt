package dev.matinzd.healthconnect

import android.app.Activity
import android.content.Intent
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import com.facebook.react.bridge.*
import dev.matinzd.healthconnect.permissions.HCPermissionManager
import dev.matinzd.healthconnect.records.ReactHealthRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthConnectManager(private val context: ReactApplicationContext) : ActivityEventListener {
  private var healthConnectClient: HealthConnectClient? = null

  companion object {
    const val REQUEST_CODE = 150
  }

  init {
    context.addActivityEventListener(this)
  }

  var pendingPromise: Promise? = null
  var latestPermissions: Set<HealthPermission>? = null

  private val isInitialized get() = healthConnectClient != null

  private inline fun throwUnlessClientIsAvailable(promise: Promise, block: () -> Unit) {
    if (!this.isAvailable()) {
      return promise.rejectWithException(ClientNotAvailable())
    }
    if (!isInitialized) {
      return promise.rejectWithException(ClientNotInitialized())
    }
    block()
  }

  override fun onActivityResult(
    activity: Activity?,
    requestCode: Int,
    resultCode: Int,
    intent: Intent?
  ) {
    if (requestCode == REQUEST_CODE) {
      pendingPromise?.resolve(HCPermissionManager.parseResult(resultCode, intent))
    }
  }

  override fun onNewIntent(intent: Intent?) {}

  fun isAvailable(): Boolean {
    return HealthConnectClient.isProviderAvailable(context)
  }

  fun initialize(promise: Promise) {
    try {
      if (healthConnectClient == null) {
        healthConnectClient = HealthConnectClient.getOrCreate(context)
      }
      promise.resolve(true)
    } catch (e: Exception) {
      promise.rejectWithException(e)
    }
  }

  fun requestPermission(reactPermissions: ReadableArray, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      this.pendingPromise = promise
      this.latestPermissions = HCPermissionManager.parsePermissions(reactPermissions)
      val intent = HCPermissionManager.healthPermissionContract.createIntent(
        context,
        latestPermissions!!
      )
      context.startActivityForResult(intent, HealthConnectManager.REQUEST_CODE, null)
    }
  }

  fun revokeAllPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      CoroutineScope(Dispatchers.IO).launch {
        healthConnectClient?.permissionController?.revokeAllPermissions()
      }
    }
  }

  fun insertRecords(reactRecords: ReadableArray, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      CoroutineScope(Dispatchers.IO).launch {
        try {
          val records = ReactHealthRecord.parseWriteRecords(reactRecords)
          val response = healthConnectClient?.insertRecords(records)
          promise.resolve(ReactHealthRecord.parseWriteResponse(response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      CoroutineScope(Dispatchers.IO).launch {
        try {
          val request = ReactHealthRecord.parseReadRequest(recordType, options)
          val response = healthConnectClient?.readRecords(request)
          promise.resolve(ReactHealthRecord.parseReadResponse(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }
}

