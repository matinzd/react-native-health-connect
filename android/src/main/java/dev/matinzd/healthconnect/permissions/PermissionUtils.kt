package dev.matinzd.healthconnect.permissions

import android.util.Log
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.InvalidRecordType
import dev.matinzd.healthconnect.utils.reactRecordTypeToClassMap

class PermissionUtils {
  companion object {
    fun parsePermissions(reactPermissions: ReadableArray): Set<String> {
      val setOfPermissions = reactPermissions.toArrayList().mapNotNull {
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

      val containsExercise = setOfPermissions.contains(HealthPermission.getWritePermission(ExerciseSessionRecord::class))
      Log.d("PermissionUtils", "Set of permissions are: " + setOfPermissions.toTypedArray().joinToString(", "))
      Log.d("PermissionUtils", "WritePermission we're checking is " + HealthPermission.getWritePermission(ExerciseSessionRecord::class))
      Log.d("PermissionUtils", "Contains is $containsExercise")

      val shouldAskForRoute = true // TODO
      if(containsExercise && shouldAskForRoute) {
        return setOfPermissions.plus(HealthPermission.PERMISSION_WRITE_EXERCISE_ROUTE)
      }
      else {
        return setOfPermissions
      }
    }

    suspend fun getGrantedPermissions(permissionController: PermissionController): WritableNativeArray {
      return mapPermissionResult(permissionController.getGrantedPermissions())
    }

    fun mapPermissionResult(grantedPermissions: Set<String>): WritableNativeArray {
      return WritableNativeArray().apply {
        for ((recordType, recordClass) in reactRecordTypeToClassMap) {
          val readPermissionForRecord = HealthPermission.getReadPermission(recordClass)
          val writePermissionForRecord = HealthPermission.getWritePermission(recordClass)

          if (grantedPermissions.contains(readPermissionForRecord)) {
            pushMap(ReactPermission(AccessType.READ, recordType).toReadableMap())
          }

          if (grantedPermissions.contains(writePermissionForRecord)) {
            pushMap(ReactPermission(AccessType.WRITE, recordType).toReadableMap())
          }
        }
      }
    }

  }
}
