package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactCyclingPedalingCadenceRecord : ReactHealthRecordImpl<CyclingPedalingCadenceRecord> {
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
        startZoneOffset = null
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
      metrics = setOf(
        CyclingPedalingCadenceRecord.RPM_AVG,
        CyclingPedalingCadenceRecord.RPM_MAX,
        CyclingPedalingCadenceRecord.RPM_MIN
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
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
}
