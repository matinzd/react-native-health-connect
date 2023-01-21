package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import java.time.Instant
import kotlin.reflect.KClass

fun <T : Record> convertReactRequestOptionsFromJS(
  recordType: KClass<T>, options: ReadableMap
): ReadRecordsRequest<T> {
  return ReadRecordsRequest(
    recordType,
    timeRangeFilter = options.getTimeRangeFilter("timeRangeFilter"),
    dataOriginFilter = convertJsToDataOriginSet(options.getArray("dataOriginFilter")),
    ascendingOrder = if (options.hasKey("ascendingOrder")) options.getBoolean("ascendingOrder") else true,
    pageSize = if (options.hasKey("pageSize")) options.getInt("pageSize") else 1000,
    pageToken = if (options.hasKey("pageToken")) options.getString("pageToken") else null,
  )
}

fun convertDataOriginsToJsArray(dataOrigin: Set<DataOrigin>): WritableNativeArray {
  return WritableNativeArray().apply {
    dataOrigin.forEach {
      pushString(it.packageName)
    }
  }
}

fun convertJsToDataOriginSet(readableArray: ReadableArray?): Set<DataOrigin> {
  if (readableArray == null) {
    return emptySet()
  }

  return readableArray.toArrayList().mapNotNull { DataOrigin(it.toString()) }.toSet()
}

fun convertProviderPackageNamesFromJS(providerPackageNames: ReadableArray): List<String> {
  return providerPackageNames.toArrayList().map { it.toString() }.toList()
}

fun ReadableArray.toMapList(): List<ReadableMap> {
  val list = mutableListOf<ReadableMap>()
  for (i in 0 until size()) {
    list.add(getMap(i))
  }
  return list
}

fun ReadableMap.getSafeInt(key: String, default: Int): Int {
  return if (this.hasKey(key)) this.getInt("measurementLocation") else default
}

fun ReadableMap.getTimeRangeFilter(key: String? = null): TimeRangeFilter {
  val timeRangeFilter = if (key != null) this.getMap(key)
    ?: throw Exception("Time range filter should be provided") else this

  val operator = timeRangeFilter.getString("operator")

  val startTime =
    if (timeRangeFilter.hasKey("startTime")) Instant.parse(timeRangeFilter.getString("startTime")) else null

  val endTime =
    if (timeRangeFilter.hasKey("endTime")) Instant.parse(timeRangeFilter.getString("endTime")) else null

  when (operator) {
    "between" -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
    "after" -> {
      if (startTime == null) {
        throw Exception("Start time should be provided")
      }

      return TimeRangeFilter.after(startTime)
    }
    "before" -> {
      if (endTime == null) {
        throw Exception("End time should be provided")
      }

      return TimeRangeFilter.before(endTime)
    }
    else -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
  }
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
  "ActiveCaloriesBurned" to ActiveCaloriesBurnedRecord::class,
  "BasalBodyTemperature" to BasalBodyTemperatureRecord::class,
  "BasalMetabolicRate" to BasalMetabolicRateRecord::class,
  "BloodGlucose" to BloodGlucoseRecord::class,
  "BloodPressure" to BloodPressureRecord::class,
  "BodyFat" to BodyFatRecord::class,
  "BoneMass" to BoneMassRecord::class,
  "CervicalMucus" to CervicalMucusRecord::class,
  "CyclingPedalingCadence" to CyclingPedalingCadenceRecord::class,
  "Distance" to DistanceRecord::class,
  "ElevationGained" to ElevationGainedRecord::class,
  "ExerciseEvent" to ExerciseEventRecord::class,
  "ExerciseLap" to ExerciseLapRecord::class,
  "ExerciseRepetitions" to ExerciseRepetitionsRecord::class,
  "ExerciseSession" to ExerciseSessionRecord::class,
  "FloorsClimbed" to FloorsClimbedRecord::class,
  "HeartRate" to HeartRateRecord::class,
  "Height" to HeightRecord::class,
  "HipCircumference" to HipCircumferenceRecord::class,
  "Hydration" to HydrationRecord::class,
  "LeanBodyMass" to LeanBodyMassRecord::class,
  "MenstruationFlow" to MenstruationFlowRecord::class,
  "Nutrition" to NutritionRecord::class,
  "OvulationTest" to OvulationTestRecord::class,
  "OxygenSaturation" to OxygenSaturationRecord::class,
  "Power" to PowerRecord::class,
  "RespiratoryRate" to RespiratoryRateRecord::class,
  "RestingHeartRate" to RestingHeartRateRecord::class,
  "SexualActivity" to SexualActivityRecord::class,
  "SleepSession" to SleepSessionRecord::class,
  "SleepStage" to SleepStageRecord::class,
  "Speed" to SpeedRecord::class,
  "StepsCadence" to StepsCadenceRecord::class,
  "Steps" to StepsRecord::class,
  "SwimmingStrokes" to SwimmingStrokesRecord::class,
  "TotalCaloriesBurned" to TotalCaloriesBurnedRecord::class,
  "Vo2Max" to Vo2MaxRecord::class,
  "WaistCircumference" to WaistCircumferenceRecord::class,
  "Weight" to WeightRecord::class,
  "WheelchairPushes" to WheelchairPushesRecord::class
)
