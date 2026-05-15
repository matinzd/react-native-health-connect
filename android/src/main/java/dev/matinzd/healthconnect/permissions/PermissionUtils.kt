package dev.matinzd.healthconnect.permissions

import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.InvalidRecordType
import dev.matinzd.healthconnect.utils.reactRecordTypeToClassMap

class PermissionUtils {
  companion object {
    private data class RecordPermissionMapping(
      val recordType: String,
      val readPermission: String,
      val writePermission: String,
    )

    private val permissionMappings: List<RecordPermissionMapping> = reactRecordTypeToClassMap.entries.map { entry ->
      RecordPermissionMapping(
        recordType = entry.key,
        readPermission = HealthPermission.getReadPermission(entry.value),
        writePermission = HealthPermission.getWritePermission(entry.value)
      )
    }

    private val readPermissionByRecordType: Map<String, String> =
      permissionMappings.associate { mapping -> mapping.recordType to mapping.readPermission }

    private val writePermissionByRecordType: Map<String, String> =
      permissionMappings.associate { mapping -> mapping.recordType to mapping.writePermission }

    fun parsePermissions(reactPermissions: ReadableArray): Set<String> {
      return reactPermissions.toArrayList().mapNotNull {
        it as HashMap<*, *>
        val recordType = it["recordType"]
        val accessType = it["accessType"]

        if (accessType == "write" && recordType == "ExerciseRoute") {
          return@mapNotNull HealthPermission.PERMISSION_WRITE_EXERCISE_ROUTE
        }

        if (accessType == "read" && recordType == "ReadHealthDataHistory") {
          return@mapNotNull HealthPermission.PERMISSION_READ_HEALTH_DATA_HISTORY
        }

        if (accessType == "read" && recordType == "BackgroundAccessPermission") {
          return@mapNotNull HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
        }

        when (accessType) {
          "write" -> writePermissionByRecordType[recordType] ?: throw InvalidRecordType()
          "read" -> readPermissionByRecordType[recordType] ?: throw InvalidRecordType()
          else -> null
        }
      }.toSet()
    }

    suspend fun getGrantedPermissions(permissionController: PermissionController): WritableNativeArray {
      return mapPermissionResult(permissionController.getGrantedPermissions())
    }

    fun mapPermissionResult(grantedPermissions: Set<String>): WritableNativeArray {
      return WritableNativeArray().apply {
        // Handle regular permissions
        for (mapping in permissionMappings) {
          val recordType = mapping.recordType

          if (grantedPermissions.contains(mapping.readPermission)) {
            pushMap(ReactPermission(AccessType.READ, recordType).toReadableMap())
          }

          if (grantedPermissions.contains(mapping.writePermission)) {
            pushMap(ReactPermission(AccessType.WRITE, recordType).toReadableMap())
          }
        }

        // Handle special permissions
        if (grantedPermissions.contains(HealthPermission.PERMISSION_WRITE_EXERCISE_ROUTE)) {
          pushMap(ReactPermission(AccessType.WRITE, "ExerciseRoute").toReadableMap())
        }

        if (grantedPermissions.contains(HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND)
        ) {
          pushMap(ReactPermission(AccessType.READ, "BackgroundAccessPermission").toReadableMap())
        }
      }
    }
  }
}
