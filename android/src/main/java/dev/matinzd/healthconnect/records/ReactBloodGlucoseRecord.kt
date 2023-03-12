package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.MealType.MEAL_TYPE_UNKNOWN
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.BloodGlucose
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactBloodGlucoseRecord : ReactHealthRecordImpl<BloodGlucoseRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BloodGlucoseRecord> {
    return records.toMapList().map {
      BloodGlucoseRecord(
        time = Instant.parse(it.getString("time")),
        level = getBloodGlucoseFromJsMap(it.getMap("level")),
        specimenSource = it.getSafeInt(
          "specimenSource", BloodGlucoseRecord.SPECIMEN_SOURCE_UNKNOWN
        ),
        mealType = it.getSafeInt("mealType", MEAL_TYPE_UNKNOWN),
        relationToMeal = it.getSafeInt(
          "relationToMeal", BloodGlucoseRecord.RELATION_TO_MEAL_UNKNOWN
        ),
        zoneOffset = null
      )
    }
  }

  override fun parseRecord(record: BloodGlucoseRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putMap("level", bloodGlucoseToJsMap(record.level))
      putInt("specimenSource", record.specimenSource)
      putInt("mealType", record.mealType)
      putInt("relationToMeal", record.relationToMeal)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }

  private fun bloodGlucoseToJsMap(bloodGlucose: BloodGlucose): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inMillimolesPerLiter", bloodGlucose.inMillimolesPerLiter)
      putDouble("inMilligramsPerDeciliter", bloodGlucose.inMilligramsPerDeciliter)
    }
  }

  private fun getBloodGlucoseFromJsMap(bloodGlucoseMap: ReadableMap?): BloodGlucose {
    if (bloodGlucoseMap == null) {
      throw InvalidBloodGlucoseLevel()
    }

    val value = bloodGlucoseMap.getDouble("value")
    return when (bloodGlucoseMap.getString("unit")) {
      "milligramsPerDeciliter" -> BloodGlucose.milligramsPerDeciliter(value)
      "millimolesPerLiter" -> BloodGlucose.millimolesPerLiter(value)
      else -> BloodGlucose.milligramsPerDeciliter(value)
    }
  }
}
