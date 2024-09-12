package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant
import java.time.Duration
import com.facebook.react.bridge.WritableNativeArray

class ReactStepsRecord : ReactHealthRecordImpl<StepsRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<StepsRecord> {
    return records.toMapList().map { map ->
      StepsRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        count = map.getDouble("count").toLong(),
        metadata = convertMetadataFromJSMap(map.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: StepsRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putDouble("count", record.count.toDouble())
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        StepsRecord.COUNT_TOTAL
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    return AggregateGroupByDurationRequest(
      metrics = setOf(StepsRecord.COUNT_TOTAL),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      timeRangeSlicer = Duration.ofMinutes(record.getInt("bucketInterval").toLong())
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("COUNT_TOTAL", record[StepsRecord.COUNT_TOTAL]?.toDouble() ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun parseAggregationResultGroupedByDuration(records: List<AggregationResultGroupedByDuration>): WritableNativeArray {
    // Create the WritableNativeArray to hold the parsed results
    val resultArray = WritableNativeArray()

    // Map each AggregationResultGroupedByDuration into a WritableNativeMap
    records.forEach { aggregationResult ->
      val value: Double = aggregationResult.result[StepsRecord.COUNT_TOTAL]?.toDouble() ?: 0.0
      val map = WritableNativeMap().apply {
          putString("startDate", aggregationResult.startTime.toString())
          putDouble("steps", value)
          putArray("dataOrigins", convertDataOriginsToJsArray(aggregationResult.result.dataOrigins))
      }

      if(value > 0) {
        // Add the map to the result array
        resultArray.pushMap(map)
      }
    }

    return resultArray
  }
}
