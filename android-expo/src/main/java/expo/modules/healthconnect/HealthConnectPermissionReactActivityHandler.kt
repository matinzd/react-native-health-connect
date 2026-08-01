package expo.modules.healthconnect

import android.app.Activity
import android.os.Bundle
import com.facebook.react.ReactActivity
import dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate
import expo.modules.core.interfaces.ReactActivityLifecycleListener

class HealthConnectPermissionReactActivityHandler : ReactActivityLifecycleListener {
  override fun onCreate(activity: Activity?, savedInstanceState: Bundle?) {
    super.onCreate(activity, savedInstanceState)
    val reactActivity = activity as? ReactActivity ?: return
    HealthConnectPermissionDelegate.setPermissionDelegate(reactActivity)
  }
}
