package dev.matinzd.healthconnect.permissions

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap

data class ReactPermission(val accessType: AccessType, val recordType: String) {
  fun toReadableMap(): ReadableMap {
    val map = WritableNativeMap()
    map.putString(ACCESS_TYPE, accessType.reactName)
    map.putString(RECORD_TYPE, recordType)
    return map
  }

  companion object {
    private const val ACCESS_TYPE = "accessType"
    private const val RECORD_TYPE = "recordType"
  }
}

enum class AccessType(val reactName: String) {
  READ("read"),
  WRITE("write")
}
