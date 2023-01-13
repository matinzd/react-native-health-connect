package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import java.time.Instant
import kotlin.reflect.KClass

fun <T : Record> convertReactRequestOptionsFromJS(
  recordType: KClass<T>,
  readableMap: ReadableMap
): ReadRecordsRequest<T> {
  return ReadRecordsRequest(
    recordType,
    timeRangeFilter = TimeRangeFilter.between(
      Instant.parse(readableMap.getString("startTime")),
      Instant.parse(readableMap.getString("endTime")),
    ),
    dataOriginFilter = readableMap.getArray("dataOriginFilter")?.toArrayList()
      ?.mapNotNull { DataOrigin(it.toString()) }?.toSet() ?: emptySet(),
    ascendingOrder = if (readableMap.hasKey("ascendingOrder")) readableMap.getBoolean("ascendingOrder") else true,
    pageSize = if (readableMap.hasKey("pageSize")) readableMap.getInt("pageSize") else 1000,
    pageToken = if (readableMap.hasKey("pageToken")) readableMap.getString("pageToken") else null,
  )
}

fun convertProviderPackageNamesFromJS(providerPackageNames: ReadableArray): List<String> {
  return providerPackageNames.toArrayList().map { it.toString() }.toList()
}

fun convertMetadataToJSMap(meta: Metadata): WritableNativeMap {
  return WritableNativeMap().apply {
    putString("id", meta.id)
    putString("clientRecordId", meta.clientRecordId)
    putDouble("clientRecordVersion", meta.clientRecordVersion.toDouble())
    putString("dataOrigin", meta.dataOrigin.packageName)
    putString("lastModifiedTime", meta.lastModifiedTime.toString())
    putInt("device", meta.device?.type ?: 0)
  }
}


val reactRecordTypeToClassMap: Map<String, KClass<out Record>> = mapOf(
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
