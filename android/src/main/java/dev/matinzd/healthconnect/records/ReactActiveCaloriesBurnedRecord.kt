package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.convertDataOriginsToJsArray
import dev.matinzd.healthconnect.utils.convertJsToDataOriginSet
import dev.matinzd.healthconnect.utils.convertMetadataFromJSMap
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.energyToJsMap
import dev.matinzd.healthconnect.utils.getEnergyFromJsMap
import dev.matinzd.healthconnect.utils.getTimeRangeFilter
import dev.matinzd.healthconnect.utils.toMapList
import dev.matinzd.healthconnect.utils.mapJsDurationToDuration
import dev.matinzd.healthconnect.utils.mapJsPeriodToPeriod
import java.time.Instant

class ReactActiveCaloriesBurnedRecord : ReactHealthRecordImpl<ActiveCaloriesBurnedRecord> {
  private val aggregateMetrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL)

  override fun parseWriteRecord(records: ReadableArray): List<ActiveCaloriesBurnedRecord> {
    return records.toMapList().map {
      ActiveCaloriesBurnedRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        energy = getEnergyFromJsMap(it.getMap("energy")),
        endZoneOffset = null,
        startZoneOffset = null,
        metadata = convertMetadataFromJSMap(it.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: ActiveCaloriesBurnedRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putMap("energy", energyToJsMap(record.energy))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = aggregateMetrics,
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    return AggregateGroupByDurationRequest(
      metrics = aggregateMetrics,
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      timeRangeSlicer = mapJsDurationToDuration(record.getMap("timeRangeSlicer")),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest {
    return AggregateGroupByPeriodRequest(
      metrics = aggregateMetrics,
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      timeRangeSlicer = mapJsPeriodToPeriod(record.getMap("timeRangeSlicer")),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      val map = WritableNativeMap().apply {
        putDouble(
          "inCalories",
          record[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inCalories ?: 0.0
        )
        putDouble(
          "inKilojoules",
          record[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilojoules ?: 0.0
        )
        putDouble(
          "inKilocalories",
          record[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilocalories ?: 0.0
        )
        putDouble(
          "inJoules",
          record[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inJoules ?: 0.0
        )
      }
      putMap("ACTIVE_CALORIES_TOTAL", map)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray {
    return WritableNativeArray().apply {
      record.forEach {
        val map = WritableNativeMap().apply {
          putMap("result", parseAggregationResult(it.result))
          putString("startTime", it.startTime.toString())
          putString("endTime", it.endTime.toString())
          putString("zoneOffset", it.zoneOffset.toString())
        }
        pushMap(map)
      }
    }
  }

  override fun parseAggregationResultGroupedByPeriod(record: List<AggregationResultGroupedByPeriod>): WritableNativeArray {
    return WritableNativeArray().apply {
      record.forEach {
        val map = WritableNativeMap().apply {
          putMap("result", parseAggregationResult(it.result))
          putString("startTime", it.startTime.toString())
          putString("endTime", it.endTime.toString())
        }
        pushMap(map)
      }
    }
  }
}
