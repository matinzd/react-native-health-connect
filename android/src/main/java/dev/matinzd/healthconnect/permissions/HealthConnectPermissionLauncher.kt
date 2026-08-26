package dev.matinzd.healthconnect.permissions

import android.content.Intent
import androidx.health.connect.client.records.ExerciseRoute
import com.facebook.react.bridge.ReactApplicationContext
import dev.matinzd.healthconnect.utils.ActivityNotAvailable
import dev.matinzd.healthconnect.utils.RequestAlreadyInProgress
import kotlinx.coroutines.CompletableDeferred

/**
 * Launches the Health Connect permission and exercise route dialogs without needing anything
 * registered up-front by the host MainActivity. The dialogs are hosted by
 * [HealthConnectPermissionActivity], a transparent activity declared by this library.
 */
class HealthConnectPermissionLauncher(private val reactContext: ReactApplicationContext) {
  private var requestInFlight = false

  suspend fun requestPermissions(
    providerPackageName: String, permissions: Set<String>
  ): Set<String> {
    val deferred = CompletableDeferred<Set<String>>()

    start(PendingHealthConnectRequest.Permissions(deferred)) {
      putExtra(
        HealthConnectPermissionActivity.EXTRA_REQUEST_TYPE,
        HealthConnectPermissionActivity.REQUEST_TYPE_PERMISSIONS
      )
      putExtra(
        HealthConnectPermissionActivity.EXTRA_PROVIDER_PACKAGE_NAME, providerPackageName
      )
      putStringArrayListExtra(
        HealthConnectPermissionActivity.EXTRA_PERMISSIONS, ArrayList(permissions)
      )
    }

    return try {
      deferred.await()
    } finally {
      requestInFlight = false
    }
  }

  suspend fun requestExerciseRoute(recordId: String): ExerciseRoute? {
    val deferred = CompletableDeferred<ExerciseRoute?>()

    start(PendingHealthConnectRequest.Route(deferred)) {
      putExtra(
        HealthConnectPermissionActivity.EXTRA_REQUEST_TYPE,
        HealthConnectPermissionActivity.REQUEST_TYPE_EXERCISE_ROUTE
      )
      putExtra(HealthConnectPermissionActivity.EXTRA_RECORD_ID, recordId)
    }

    return try {
      deferred.await()
    } finally {
      requestInFlight = false
    }
  }

  private fun start(request: PendingHealthConnectRequest, configure: Intent.() -> Unit) {
    if (requestInFlight) throw RequestAlreadyInProgress()

    val activity = reactContext.currentActivity ?: throw ActivityNotAvailable()

    requestInFlight = true
    HealthConnectPermissionActivity.setPendingRequest(request)

    try {
      activity.startActivity(
        Intent(activity, HealthConnectPermissionActivity::class.java).apply(configure)
      )
    } catch (e: Exception) {
      HealthConnectPermissionActivity.setPendingRequest(null)
      requestInFlight = false
      throw e
    }
  }
}
