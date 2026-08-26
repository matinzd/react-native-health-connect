package dev.matinzd.healthconnect.permissions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.contracts.ExerciseRouteRequestContract
import androidx.health.connect.client.records.ExerciseRoute
import dev.matinzd.healthconnect.HealthConnectManager
import kotlinx.coroutines.CompletableDeferred

internal sealed class PendingHealthConnectRequest {
  class Permissions(val deferred: CompletableDeferred<Set<String>>) : PendingHealthConnectRequest()
  class Route(val deferred: CompletableDeferred<ExerciseRoute?>) : PendingHealthConnectRequest()
}

/**
 * Transparent activity that owns the ActivityResultRegistry the Health Connect contracts need.
 *
 * The contracts cannot be launched with a plain startActivityForResult: on Android 14+ the
 * permission contract produces a synthetic intent that only an ActivityResultRegistry can
 * service. Hosting our own ComponentActivity gives us that registry without asking consumers
 * to touch their MainActivity.
 */
class HealthConnectPermissionActivity : ComponentActivity() {
  private lateinit var requestPermissions: ActivityResultLauncher<Set<String>>
  private lateinit var requestExerciseRoute: ActivityResultLauncher<String>

  private var resultDelivered = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val providerPackageName = intent.getStringExtra(EXTRA_PROVIDER_PACKAGE_NAME)
      ?: HealthConnectManager.DEFAULT_PROVIDER_PACKAGE_NAME

    requestPermissions = registerForActivityResult(
      PermissionController.createRequestPermissionResultContract(providerPackageName)
    ) { granted ->
      (takePendingRequest() as? PendingHealthConnectRequest.Permissions)?.deferred?.complete(granted)
      resultDelivered = true
      finish()
    }

    requestExerciseRoute = registerForActivityResult(ExerciseRouteRequestContract()) { route ->
      (takePendingRequest() as? PendingHealthConnectRequest.Route)?.deferred?.complete(route)
      resultDelivered = true
      finish()
    }

    // On recreation the registry replays the pending result to the launchers above, so we must
    // not launch a second dialog.
    if (savedInstanceState != null) return

    when (intent.getStringExtra(EXTRA_REQUEST_TYPE)) {
      REQUEST_TYPE_PERMISSIONS -> requestPermissions.launch(
        intent.getStringArrayListExtra(EXTRA_PERMISSIONS)?.toSet() ?: emptySet()
      )

      REQUEST_TYPE_EXERCISE_ROUTE -> requestExerciseRoute.launch(
        intent.getStringExtra(EXTRA_RECORD_ID) ?: ""
      )

      else -> finish()
    }
  }

  override fun onDestroy() {
    // Make sure a dismissed or killed dialog never leaves the JS promise hanging.
    if (isFinishing && !resultDelivered) {
      when (val request = takePendingRequest()) {
        is PendingHealthConnectRequest.Permissions -> request.deferred.complete(emptySet())
        is PendingHealthConnectRequest.Route -> request.deferred.complete(null)
        null -> Unit
      }
    }
    super.onDestroy()
  }

  companion object {
    const val EXTRA_REQUEST_TYPE = "requestType"
    const val EXTRA_PROVIDER_PACKAGE_NAME = "providerPackageName"
    const val EXTRA_PERMISSIONS = "permissions"
    const val EXTRA_RECORD_ID = "recordId"

    const val REQUEST_TYPE_PERMISSIONS = "permissions"
    const val REQUEST_TYPE_EXERCISE_ROUTE = "exerciseRoute"

    private val lock = Any()
    private var pendingRequest: PendingHealthConnectRequest? = null

    internal fun setPendingRequest(request: PendingHealthConnectRequest?) = synchronized(lock) {
      pendingRequest = request
    }

    internal fun takePendingRequest(): PendingHealthConnectRequest? = synchronized(lock) {
      pendingRequest.also { pendingRequest = null }
    }
  }
}
