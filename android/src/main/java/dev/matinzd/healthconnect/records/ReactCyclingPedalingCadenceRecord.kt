package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactCyclingPedalingCadenceRecord : ReactHealthRecordImpl<CyclingPedalingCadenceRecord> {
  private val aggregateMetrics = setOf(
    CyclingPedalingCadenceRecord.RPM_AVG,
    CyclingPedalingCadenceRecord.RPM_MAX,
    CyclingPedalingCadenceRecord.RPM_MIN
  )

  override fun parseWriteRecord(records: ReadableArray): List<CyclingPedalingCadenceRecord> {
    return records.toMapList().map { map ->
      CyclingPedalingCadenceRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        samples = map.getArray("samples")?.toMapList()?.map { sample ->
          CyclingPedalingCadenceRecord.Sample(
            time = Instant.parse(sample.getString("time")),
            revolutionsPerMinute = sample.getDouble("revolutionsPerMinute")
          )
        } ?: emptyList(),
        endZoneOffset = null,
        startZoneOffset = null,
        metadata = convertMetadataFromJSMap(map.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: CyclingPedalingCadenceRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      val array = WritableNativeArray().apply {
        record.samples.map {
          val map = WritableNativeMap()
          map.putString("time", it.time.toString())
          map.putDouble("revolutionsPerMinute", it.revolutionsPerMinute)
          this.pushMap(map)
        }
      }
      putArray("samples", array)
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
      putDouble("RPM_AVG", record[CyclingPedalingCadenceRecord.RPM_AVG] ?: 0.0)
      putDouble("RPM_MAX", record[CyclingPedalingCadenceRecord.RPM_MAX] ?: 0.0)
      putDouble("RPM_MIN", record[CyclingPedalingCadenceRecord.RPM_MIN] ?: 0.0)
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
