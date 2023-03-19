package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactRestingHeartRateRecord : ReactHealthRecordImpl<RestingHeartRateRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<RestingHeartRateRecord> {
    return records.toMapList().map { map ->
      RestingHeartRateRecord(
        time = Instant.parse(map.getString("time")),
        zoneOffset = null,
        beatsPerMinute = map.getDouble("beatsPerMinute").toLong()
      )
    }
  }

  override fun parseRecord(record: RestingHeartRateRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("beatsPerMinute", record.beatsPerMinute.toDouble())
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        RestingHeartRateRecord.BPM_AVG,
        RestingHeartRateRecord.BPM_MAX,
        RestingHeartRateRecord.BPM_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
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
}
