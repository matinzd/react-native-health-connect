package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactWeightRecord : ReactHealthRecordImpl<WeightRecord> {
  private val aggregateMetrics = setOf(
    WeightRecord.WEIGHT_AVG,
    WeightRecord.WEIGHT_MAX,
    WeightRecord.WEIGHT_MIN,
  )

  override fun parseWriteRecord(records: ReadableArray): List<WeightRecord> {
    return records.toMapList().map { map ->
      WeightRecord(
        time = Instant.parse(map.getString("time")),
        weight = getMassFromJsMap(map.getMap("weight")),
        zoneOffset = null,
        metadata = convertMetadataFromJSMap(map.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: WeightRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putInstantRecordTime(
        time = record.time,
        zoneOffset = record.zoneOffset
      )
      putMap("weight", massToJsMap(record.weight))
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
      putMap("WEIGHT_AVG", massToJsMap(record[WeightRecord.WEIGHT_AVG]))
      putMap("WEIGHT_MAX", massToJsMap(record[WeightRecord.WEIGHT_MAX]))
      putMap("WEIGHT_MIN", massToJsMap(record[WeightRecord.WEIGHT_MIN]))
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
