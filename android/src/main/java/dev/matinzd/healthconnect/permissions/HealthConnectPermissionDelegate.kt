package dev.matinzd.healthconnect.permissions

import androidx.activity.ComponentActivity
import dev.matinzd.healthconnect.HealthConnectManager

/**
 * No longer required. Permission and exercise route dialogs are now launched through
 * [HealthConnectPermissionLauncher], which does not need anything registered by the host
 * activity. Kept as a no-op so existing MainActivity code keeps compiling; it will be
 * removed in a future release.
 */
@Deprecated(
  "No longer needed. Remove the setPermissionDelegate call from your MainActivity.",
  level = DeprecationLevel.WARNING
)
object HealthConnectPermissionDelegate {
  @Suppress("UNUSED_PARAMETER")
  fun setPermissionDelegate(
    activity: ComponentActivity,
    providerPackageName: String = HealthConnectManager.DEFAULT_PROVIDER_PACKAGE_NAME
  ) {
    // no-op
  }
}
