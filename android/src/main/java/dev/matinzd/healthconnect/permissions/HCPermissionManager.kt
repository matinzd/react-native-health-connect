package dev.matinzd.healthconnect.permissions

import android.content.Intent
import android.util.Log
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.HealthConnectModule
import dev.matinzd.healthconnect.InvalidRecordType
import kotlin.reflect.KClass

class HCPermissionManager {
  companion object {
    val healthPermissionContract =
      PermissionController.createRequestPermissionResultContract()

    private val permissionClassMap: Map<String, KClass<out Record>> = mapOf(
      "activeCaloriesBurned" to ActiveCaloriesBurnedRecord::class,
      "basalBodyTemperature" to BasalBodyTemperatureRecord::class,
      "basalMetabolicRate" to BasalMetabolicRateRecord::class,
      "bloodGlucose" to BloodGlucoseRecord::class,
    )

    fun parsePermissions(reactPermissions: ReadableArray): Set<HealthPermission> {
      return reactPermissions.toArrayList().mapNotNull { it as HashMap<*, *>
        val recordType = it["recordType"]
        val recordClass = permissionClassMap[recordType]
          ?: throw InvalidRecordType()

        when (it["accessType"]) {
          "write" -> HealthPermission.createWritePermission(recordClass)
          "read" -> HealthPermission.createReadPermission(recordClass)
          else -> null
        }
      }.toSet()
    }

    fun parseResult(resultCode: Int, intent: Intent?): WritableNativeArray {
      val results = healthPermissionContract.parseResult(resultCode, intent)
      val permissions = WritableNativeArray()


      results.forEach {
        //TODO: parse incoming results and pass it back to javascript
        Log.d(HealthConnectModule.NAME, it.toString())
      }

      return permissions
    }
  }
}
