package dev.matinzd.healthconnect.permissions

import android.util.Log
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.InvalidRecordType
import dev.matinzd.healthconnect.utils.UnsupportedPermissionType
import dev.matinzd.healthconnect.utils.reactRecordTypeToClassMap
import java.util.logging.Logger

class PermissionUtils {
  companion object {
    fun parsePermissions(reactPermissions: ReadableArray): Set<String> {
      return reactPermissions.toArrayList().mapNotNull {
        it as HashMap<*, *>
        val recordType = it["recordType"]
        val recordClass = reactRecordTypeToClassMap[recordType]
          ?: throw InvalidRecordType()

        when (it["accessType"]) {
          "write" -> HealthPermission.getWritePermission(recordClass)
          "read" -> HealthPermission.getReadPermission(recordClass)
          else -> null
        }
      }.toSet()
    }

    suspend fun getGrantedPermissions(permissionController: PermissionController): WritableNativeArray {
      return mapPermissionResult(permissionController.getGrantedPermissions())
    }

    fun mapPermissionResult(grantedPermissions: Set<String>): WritableNativeArray {
      return WritableNativeArray().apply {
        grantedPermissions.forEach {
          val map = WritableNativeMap()

          try {
            val (accessType, recordType) = extractPermissionResult(it)
            map.putString("recordType", recordType)
            map.putString("accessType", accessType)
            pushMap(map)
          }
          catch (e: UnsupportedPermissionType) {
            Log.d("Unsupported Permission", "Encountered an unsupported permission type: $it")
          }
        }
      }
    }

    private fun extractPermissionResult(permissionName: String): Pair<String, String>  {
      for((recordType, recordClass) in reactRecordTypeToClassMap) {
          val readPermissionForRecord = HealthPermission.getReadPermission(recordClass)
          if(readPermissionForRecord == permissionName) {
            return Pair("read", recordType)
          }

        val writePermissionForRecord = HealthPermission.getWritePermission(recordClass)
        if(writePermissionForRecord == permissionName) {
          return Pair("write", recordType)
        }
      }

      throw UnsupportedPermissionType()
    }
  }
}
