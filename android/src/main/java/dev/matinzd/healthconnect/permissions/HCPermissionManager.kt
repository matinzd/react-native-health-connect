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

    fun parsePermissions(reactPermissions: ReadableArray): Set<HealthPermission> {
      return reactPermissions.toArrayList().mapNotNull {
        it as HashMap<*, *>
        val recordType = it["recordType"]
        val recordClass = reactRecordTypeToClassMap[recordType]
          ?: throw InvalidRecordType()

        when (it["accessType"]) {
          "write" -> HealthPermission.createWritePermission(recordClass)
          "read" -> HealthPermission.createReadPermission(recordClass)
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

      val grantedPermissions = WritableNativeArray().apply {
        result.forEach {
          val map = WritableNativeMap()
          // TODO: Find a way to properly parse permission result without suppression
          @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
          map.putString("recordType", it.recordType.simpleName)
          @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
          map.putInt("accessType", it.accessType)
          pushMap(map)
        }
      }

      pendingPromise?.resolve(grantedPermissions)
    }
  }
}
