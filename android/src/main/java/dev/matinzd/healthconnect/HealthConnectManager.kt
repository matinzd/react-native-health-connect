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

  var permissionPromise: Promise? = null
  var latestPermissions: Set<HealthPermission>? = null

  val isInitialized get() = healthConnectClient != null

  private inline fun throwUnlessClientIsAvailable(promise: Promise, block: () -> Unit) {
    if (!this.isAvailable()) {
      return HealthConnectException.rejectPromiseWithException(promise, ClientNotAvailable())
    }
    if (this.healthConnectClient == null) {
      return HealthConnectException.rejectPromiseWithException(promise, ClientNotInitialized())
    }
    block()
  }

  private fun getExceptionCode(exception: Exception): String {
    return when (exception) {
      is UnsupportedOperationException -> {
        "SDK_VERSION_ERROR";
      }
      is IllegalStateException -> {
        "NOT_AVAILABLE"
      }
      else -> {
        "UNKNOWN_ERROR"
      }
    }
  }

  override fun onActivityResult(
    activity: Activity?,
    requestCode: Int,
    resultCode: Int,
    intent: Intent?
  ) {
    if (requestCode == REQUEST_CODE) {
      permissionPromise?.resolve(HCPermissionManager.parseResult(resultCode, intent))
    }
  }

  override fun onNewIntent(p0: Intent?) {
    // TODO
  }

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
      promise.reject(getExceptionCode(e), e.message)
    }
  }

  fun requestPermission(reactPermissions: ReadableArray, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      this.permissionPromise = promise
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
          HealthConnectException.rejectPromiseWithException(promise, e)
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
          HealthConnectException.rejectPromiseWithException(promise, e)
        }
      }
    }
  }
}

