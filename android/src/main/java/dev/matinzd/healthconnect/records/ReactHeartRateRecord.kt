package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactHeartRateRecord : ReactHealthRecordImpl<HeartRateRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<HeartRateRecord> {
    return records.toMapList().map { map ->
      HeartRateRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        samples = map.getArray("samples")?.toMapList()?.map { sample ->
          HeartRateRecord.Sample(
            time = Instant.parse(sample.getString("time")),
            beatsPerMinute = sample.getDouble("beatsPerMinute").toLong()
          )
        } ?: emptyList(),
      )
    }
  }

  override fun parseRecord(record: HeartRateRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      val array = WritableNativeArray().apply {
        record.samples.map {
          val map = WritableNativeMap()
          map.putString("time", it.time.toString())
          map.putDouble("beatsPerMinute", it.beatsPerMinute.toDouble())
          this.pushMap(map)
        }
      }
      putArray("samples", array)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        HeartRateRecord.BPM_AVG,
        HeartRateRecord.BPM_MAX,
        HeartRateRecord.BPM_MIN,
        HeartRateRecord.MEASUREMENTS_COUNT
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("BPM_AVG", record[HeartRateRecord.BPM_AVG]?.toDouble() ?: 0.0)
      putDouble("BPM_MAX", record[HeartRateRecord.BPM_MAX]?.toDouble() ?: 0.0)
      putDouble("BPM_MIN", record[HeartRateRecord.BPM_MIN]?.toDouble() ?: 0.0)
      putDouble("MEASUREMENTS_COUNT", record[HeartRateRecord.MEASUREMENTS_COUNT]?.toDouble() ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
