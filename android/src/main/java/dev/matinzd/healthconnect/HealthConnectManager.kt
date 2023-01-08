package dev.matinzd.healthconnect

import android.app.Activity
import android.content.Intent
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import dev.matinzd.healthconnect.permissions.HCPermissionManager
import dev.matinzd.healthconnect.records.ReactRecordParser
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

  private inline fun throwUnlessClientInitialized(block: () -> Unit) {
    if (this.healthConnectClient == null) {
      throw ClientNotInitialized()
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
    permissionPromise?.resolve(HCPermissionManager.parseResult(resultCode, intent))
  }

  override fun onNewIntent(p0: Intent?) {
    // TODO
  }

  fun isAvailable(): Boolean {
    return HealthConnectClient.isProviderAvailable(context)
  }

  fun initialize(promise: Promise) {
    try {
      healthConnectClient = HealthConnectClient.getOrCreate(context)
      promise.resolve(true)
    } catch (e: Exception) {
      promise.reject(getExceptionCode(e), e.message)
    }
  }

  fun requestPermission(reactPermissions: ReadableArray, promise: Promise) {
    this.permissionPromise = promise
    this.latestPermissions = HCPermissionManager.parsePermissions(reactPermissions)
    val intent = HCPermissionManager.healthPermissionContract.createIntent(
      context,
      latestPermissions!!
    )
    context.startActivityForResult(intent, HealthConnectManager.REQUEST_CODE, null)
  }

  fun revokeAllPermissions() {
    throwUnlessClientInitialized {
      CoroutineScope(Dispatchers.IO).launch {
        healthConnectClient?.permissionController?.revokeAllPermissions()
      }
    }
  }

  fun insertRecords(reactRecords: ReadableArray, promise: Promise) {
    throwUnlessClientInitialized {
      CoroutineScope(Dispatchers.IO).launch {
        val response =
          healthConnectClient?.insertRecords(ReactRecordParser.parseRecords(reactRecords))

        promise.resolve(ReactRecordParser.parseResponse(response))
      }
    }
  }
}

