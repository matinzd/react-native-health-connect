package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactFloorsClimbedRecord : ReactHealthRecordImpl<FloorsClimbedRecord> {
  private val aggregateMetrics = setOf(FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL)

  override fun parseWriteRecord(records: ReadableArray): List<FloorsClimbedRecord> {
    return records.toMapList().map {
      FloorsClimbedRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        floors = it.getDouble("floors"),
        metadata = convertMetadataFromJSMap(it.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: FloorsClimbedRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putIntervalRecordTime(
        startTime = record.startTime,
        endTime = record.endTime,
        startZoneOffset = record.startZoneOffset,
        endZoneOffset = record.endZoneOffset
      )
      putDouble("floors", record.floors)
      putMetadata(record.metadata)
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return createAggregateRequest(
      record = record,
      aggregateMetrics = aggregateMetrics
    )
  }

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    return createAggregateGroupByDurationRequest(
      record = record,
      aggregateMetrics = aggregateMetrics
    )
  }

  override fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest {
    return createAggregateGroupByPeriodRequest(
      record = record,
      aggregateMetrics = aggregateMetrics
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("FLOORS_CLIMBED_TOTAL", record[FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL] ?: 0.0)
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
