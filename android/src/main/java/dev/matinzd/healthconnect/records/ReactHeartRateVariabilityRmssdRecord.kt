package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataFromJSMap
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactHeartRateVariabilityRmssdRecord :
  ReactHealthRecordImpl<HeartRateVariabilityRmssdRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<HeartRateVariabilityRmssdRecord> {
    return records.toMapList().map { map ->
      HeartRateVariabilityRmssdRecord(
        time = Instant.parse(map.getString("time")),
        heartRateVariabilityMillis = map.getDouble("heartRateVariabilityMillis"),
        zoneOffset = null,
        metadata = convertMetadataFromJSMap(map.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: HeartRateVariabilityRmssdRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("heartRateVariabilityMillis", record.heartRateVariabilityMillis)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    throw AggregationNotSupported()
  }

  override fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResultGroupedByPeriod(record: List<AggregationResultGroupedByPeriod>): WritableNativeArray {
    throw AggregationNotSupported()
  }
}
