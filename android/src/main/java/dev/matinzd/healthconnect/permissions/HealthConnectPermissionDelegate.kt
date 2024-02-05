package dev.matinzd.healthconnect.permissions

import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import com.facebook.react.ReactActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

object HealthConnectPermissionDelegate {
  private lateinit var requestPermission: ActivityResultLauncher<Set<String>>
  private val channel = Channel<Set<String>>()
  private val coroutineScope = CoroutineScope(Dispatchers.IO)

  fun setPermissionDelegate(
    activity: ReactActivity,
    providerPackageName: String = "com.google.android.apps.healthdata"
  ) {
    val contract = PermissionController.createRequestPermissionResultContract(providerPackageName)

    requestPermission = activity.registerForActivityResult(contract) {
      coroutineScope.launch {
        channel.send(it)
        coroutineContext.cancel()
      }
    }
  }

  suspend fun launch(permissions: Set<String>): Set<String> {
    requestPermission.launch(permissions)
    return channel.receive()
  }
}
