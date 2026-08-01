package expo.modules.healthconnect

import android.content.Context
import expo.modules.core.interfaces.Package
import expo.modules.core.interfaces.ReactActivityLifecycleListener

class HealthConnectPackage : Package {
  override fun createReactActivityLifecycleListeners(activityContext: Context?): MutableList<out ReactActivityLifecycleListener> {
    return mutableListOf(HealthConnectPermissionReactActivityHandler())
  }
}
