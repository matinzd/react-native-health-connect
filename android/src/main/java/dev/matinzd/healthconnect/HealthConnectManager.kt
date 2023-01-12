package dev.matinzd.healthconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import com.facebook.react.bridge.*
import dev.matinzd.healthconnect.permissions.HCPermissionManager
import dev.matinzd.healthconnect.records.ReactHealthRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthConnectManager(private val context: ReactApplicationContext) : ActivityEventListener {
  private lateinit var healthConnectClient: HealthConnectClient

  companion object {
    const val REQUEST_CODE = 150
  }

  init {
    context.addActivityEventListener(this)
  }

  var pendingPromise: Promise? = null
  var latestPermissions: Set<HealthPermission>? = null

  private val isInitialized get() = this::healthConnectClient.isInitialized

  private inline fun throwUnlessClientIsAvailable(promise: Promise, block: () -> Unit) {
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
      HCPermissionManager.parseOnActivityResult(resultCode, intent, pendingPromise)
    }
  }

  override fun onNewIntent(intent: Intent?) {}

  fun isAvailable(providerPackageNames: ReadableArray, promise: Promise) {
    val available = HealthConnectClient.isProviderAvailable(
      context,
      convertProviderPackageNamesFromJS(providerPackageNames)
    )
    return promise.resolve(available)
  }

  fun initialize(providerPackageNames: ReadableArray, promise: Promise) {
    if (isInitialized) {
      return promise.resolve(true)
    }

    try {
      healthConnectClient = HealthConnectClient.getOrCreate(
        context,
        convertProviderPackageNamesFromJS(providerPackageNames)
      )
      promise.resolve(true)
    } catch (e: Exception) {
      promise.rejectWithException(e)
    }
  }

  fun requestPermission(
    reactPermissions: ReadableArray,
    providerPackageName: String,
    promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {

      this.pendingPromise = promise
      this.latestPermissions = HCPermissionManager.parsePermissions(reactPermissions)

      val bundle = Bundle()
      bundle.putString("providerPackageName", providerPackageName)

      val intent = HCPermissionManager(providerPackageName).healthPermissionContract.createIntent(
        context,
        latestPermissions!!
      )
      
      context.startActivityForResult(intent, HealthConnectManager.REQUEST_CODE, bundle)
    }
  }

  fun revokeAllPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      CoroutineScope(Dispatchers.IO).launch {
        healthConnectClient.permissionController.revokeAllPermissions()
      }
    }
  }

  fun insertRecords(reactRecords: ReadableArray, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      CoroutineScope(Dispatchers.IO).launch {
        try {
          val records = ReactHealthRecord.parseWriteRecords(reactRecords)
          val response = healthConnectClient.insertRecords(records)
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
          val response = healthConnectClient.readRecords(request)
          promise.resolve(ReactHealthRecord.parseReadResponse(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }
}

