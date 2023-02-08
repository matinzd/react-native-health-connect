package dev.matinzd.healthconnect.permissions

import android.content.Intent
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.InvalidRecordType
import dev.matinzd.healthconnect.utils.reactRecordTypeToClassMap

class HCPermissionManager(providerPackageName: String) {
  val healthPermissionContract =
    PermissionController.createRequestPermissionResultContract(providerPackageName)

  companion object {
    private const val DEFAULT_PROVIDER_PACKAGE_NAME = "com.google.android.apps.healthdata"

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

    fun parseOnActivityResult(
      resultCode: Int,
      intent: Intent?,
      pendingPromise: Promise?
    ) {
      val providerPackageName =
        intent?.getStringExtra("providerPackageName") ?: DEFAULT_PROVIDER_PACKAGE_NAME
      val contract = HCPermissionManager(providerPackageName).healthPermissionContract
      val result = contract.parseResult(
        resultCode,
        intent
      )

      pendingPromise?.resolve(mapPermissionResult(result))
    }

    suspend fun getGrantedPermissions(permissionController: PermissionController): WritableNativeArray {
      return mapPermissionResult(permissionController.getGrantedPermissions())
    }

    private fun mapPermissionResult(grantedPermissions: Set<String>): WritableNativeArray {
      return WritableNativeArray().apply {
        grantedPermissions.forEach {
          val map = WritableNativeMap()

          val (accessType, recordType) = extractPermissionResult(it)

          map.putString("recordType", snakeToCamel(recordType))
          map.putString("accessType", accessType)
          pushMap(map)
        }
      }
    }

    private fun extractPermissionResult(it: String): Pair<String, String> {
      val accessType = it.substring(it.lastIndexOf(".") + 1, it.indexOf("_")).lowercase()
      val recordType = it.substring(it.indexOf("_") + 1).lowercase()
      return Pair(accessType, recordType)
    }

    private fun snakeToCamel(word: String): String {
      val components = word.split("_")
      return components.joinToString("") { it[0].uppercase() + it.substring(1) }
    }
  }
}
