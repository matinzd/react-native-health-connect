package dev.matinzd.healthconnect.permissions

import android.content.Intent
import android.util.Log
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.InvalidRecordType
import kotlin.reflect.KClass

class HCPermissionManager(providerPackageName: String) {
  val healthPermissionContract =
    PermissionController.createRequestPermissionResultContract(providerPackageName)

  companion object {
    private const val DEFAULT_PROVIDER_PACKAGE_NAME = "com.google.android.apps.healthdata"

    private val permissionClassMap: Map<String, KClass<out Record>> = mapOf(
      "activeCaloriesBurned" to ActiveCaloriesBurnedRecord::class,
      "basalBodyTemperature" to BasalBodyTemperatureRecord::class,
      "basalMetabolicRate" to BasalMetabolicRateRecord::class,
      "bloodGlucose" to BloodGlucoseRecord::class,
      "bloodPressure" to BloodPressureRecord::class,
      "bodyFat" to BodyFatRecord::class,
      "boneMass" to BoneMassRecord::class,
      "cervicalMucus" to CervicalMucusRecord::class,
      "cyclingPedalingCadence" to CyclingPedalingCadenceRecord::class,
      "distance" to DistanceRecord::class,
      "elevationGained" to ElevationGainedRecord::class,
      "exerciseEvent" to ExerciseEventRecord::class,
      "exerciseLap" to ExerciseLapRecord::class,
      "exerciseRepetitions" to ExerciseRepetitionsRecord::class,
      "exerciseSession" to ExerciseSessionRecord::class,
      "floorsClimbed" to FloorsClimbedRecord::class,
      "heartRate" to HeartRateRecord::class,
      "height" to HeightRecord::class,
      "hipCircumference" to HipCircumferenceRecord::class,
      "hydration" to HydrationRecord::class,
      "leanBodyMass" to LeanBodyMassRecord::class,
      "menstruationFlow" to MenstruationFlowRecord::class,
      "nutrition" to NutritionRecord::class,
      "ovulationTest" to OvulationTestRecord::class,
      "oxygenSaturation" to OxygenSaturationRecord::class,
      "power" to PowerRecord::class,
      "respiratoryRate" to RespiratoryRateRecord::class,
      "restingHeartRate" to RestingHeartRateRecord::class,
      "sexualActivity" to SexualActivityRecord::class,
      "sleepSession" to SleepSessionRecord::class,
      "sleepStage" to SleepStageRecord::class,
      "speed" to SpeedRecord::class,
      "stepsCadence" to StepsCadenceRecord::class,
      "steps" to StepsRecord::class,
      "swimmingStrokes" to SwimmingStrokesRecord::class,
      "totalCaloriesBurned" to TotalCaloriesBurnedRecord::class,
      "vo2Max" to Vo2MaxRecord::class,
      "waistCircumference" to WaistCircumferenceRecord::class,
      "weight" to WeightRecord::class,
      "wheelchairPushes" to WheelchairPushesRecord::class
    )

    fun parsePermissions(reactPermissions: ReadableArray): Set<HealthPermission> {
      return reactPermissions.toArrayList().mapNotNull {
        it as HashMap<*, *>
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
