package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Power
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.InvalidPower
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactBasalMetabolicRateRecord : ReactHealthRecordImpl<BasalMetabolicRateRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BasalMetabolicRateRecord> {
    return records.toMapList().map {
      BasalMetabolicRateRecord(
        time = Instant.parse(it.getString("time")),
        basalMetabolicRate = getPowerFromJsMap(it.getMap("basalMetabolicRate")),
        zoneOffset = null
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BasalMetabolicRateRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putMap("basalMetabolicRate", powerToJsMap(record.basalMetabolicRate))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<BasalMetabolicRateRecord> {
    return convertReactRequestOptionsFromJS(BasalMetabolicRateRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL),
      timeRangeFilter = TimeRangeFilter.between(
        Instant.parse(record.getString("startTime")),
        Instant.parse(record.getString("endTime"))
      )
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble(
        "inKilojoules",
        record[BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL]!!.inKilojoules
      )
      putDouble(
        "inCalories",
        record[BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL]!!.inCalories
      )
      putDouble(
        "inJoules",
        record[BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL]!!.inJoules
      )
      putDouble(
        "inKilocalories",
        record[BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL]!!.inKilocalories
      )
    }
  }

  private fun powerToJsMap(power: Power): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inKilocaloriesPerDay", power.inKilocaloriesPerDay)
      putDouble("inWatts", power.inWatts)
    }
  }

  private fun getPowerFromJsMap(powerMap: ReadableMap?): Power {
    if (powerMap == null) {
      throw InvalidPower()
    }

    val value = powerMap.getDouble("value")
    return when (powerMap.getString("unit")) {
      "kilocaloriesPerDay" -> Power.kilocaloriesPerDay(value)
      "watts" -> Power.watts(value)
      else -> Power.kilocaloriesPerDay(value)
    }
  }
}
