package dev.matinzd.healthconnect.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.contracts.ExerciseRouteRequestContract
import androidx.health.connect.client.records.ExerciseRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

object HealthConnectPermissionDelegate {
  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  private val permissionsChannel = Channel<Set<String>>()
  private val exerciseRouteChannel = Channel<ExerciseRoute?>()

  private lateinit var requestPermission: ActivityResultLauncher<Set<String>>
  private lateinit var requestRoutePermission: ActivityResultLauncher<String>

  fun setPermissionDelegate(
    activity: ComponentActivity,
    providerPackageName: String = "com.google.android.apps.healthdata"
  ) {
    val contract = PermissionController.createRequestPermissionResultContract(providerPackageName)
    val exerciseRouteRequestContract = ExerciseRouteRequestContract()

    requestPermission = activity.registerForActivityResult(contract) {
      coroutineScope.launch {
        permissionsChannel.send(it)
        coroutineContext.cancel()
      }
    }

    requestRoutePermission = activity.registerForActivityResult(exerciseRouteRequestContract) {
      coroutineScope.launch {
        exerciseRouteChannel.send(it)
        coroutineContext.cancel()
      }
    }
  }

  suspend fun launchPermissionsDialog(permissions: Set<String>): Set<String> {
    requestPermission.launch(permissions)
    return permissionsChannel.receive()
  }

  suspend fun launchExerciseRouteAccessRequestDialog(recordId: String): ExerciseRoute? {
    requestRoutePermission.launch(recordId)
    return exerciseRouteChannel.receive()
  }
}
