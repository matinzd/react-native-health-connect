package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
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

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }
}
