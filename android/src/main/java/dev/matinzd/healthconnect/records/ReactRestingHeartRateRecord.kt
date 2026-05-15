package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactRestingHeartRateRecord : ReactHealthRecordImpl<RestingHeartRateRecord> {
  private val aggregateMetrics = setOf(
    RestingHeartRateRecord.BPM_AVG,
    RestingHeartRateRecord.BPM_MAX,
    RestingHeartRateRecord.BPM_MIN,
  )

  override fun parseWriteRecord(records: ReadableArray): List<RestingHeartRateRecord> {
    return records.toMapList().map { map ->
      RestingHeartRateRecord(
        time = Instant.parse(map.getString("time")),
        zoneOffset = null,
        beatsPerMinute = map.getDouble("beatsPerMinute").toLong(),
        metadata = convertMetadataFromJSMap(map.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: RestingHeartRateRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putInstantRecordTime(
        time = record.time,
        zoneOffset = record.zoneOffset
      )
      putDouble("beatsPerMinute", record.beatsPerMinute.toDouble())
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
      putDouble("BPM_AVG", record[RestingHeartRateRecord.BPM_AVG]?.toDouble() ?: 0.0)
      putDouble("BPM_MAX", record[RestingHeartRateRecord.BPM_MAX]?.toDouble() ?: 0.0)
      putDouble("BPM_MIN", record[RestingHeartRateRecord.BPM_MIN]?.toDouble() ?: 0.0)
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
