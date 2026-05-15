package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactElevationGainedRecord : ReactHealthRecordImpl<ElevationGainedRecord> {
  private val aggregateMetrics = setOf(ElevationGainedRecord.ELEVATION_GAINED_TOTAL)

  override fun parseWriteRecord(records: ReadableArray): List<ElevationGainedRecord> {
    return records.toMapList().map {
      ElevationGainedRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        elevation = getLengthFromJsMap(it.getMap("elevation")),
        metadata = convertMetadataFromJSMap(it.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: ElevationGainedRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putIntervalRecordTime(
        startTime = record.startTime,
        endTime = record.endTime,
        startZoneOffset = record.startZoneOffset,
        endZoneOffset = record.endZoneOffset
      )
      putMap("elevation", lengthToJsMap(record.elevation))
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
      putMap("ELEVATION_GAINED_TOTAL", lengthToJsMap(record[ElevationGainedRecord.ELEVATION_GAINED_TOTAL]))
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
