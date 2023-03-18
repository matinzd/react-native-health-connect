package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactSpeedRecord : ReactHealthRecordImpl<SpeedRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<SpeedRecord> {
    return records.toMapList().map { map ->
      SpeedRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        samples = map.getArray("samples")?.toMapList()?.map { sample ->
          SpeedRecord.Sample(
            time = Instant.parse(sample.getString("time")),
            speed = getVelocityFromJsMap(sample.getMap("speed"))
          )
        } ?: emptyList(),
      )
    }
  }

  override fun parseRecord(record: SpeedRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      val array = WritableNativeArray().apply {
        record.samples.map {
          val map = WritableNativeMap()
          map.putString("time", it.time.toString())
          map.putMap("speed", velocityToJsMap(it.speed))
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
        SpeedRecord.SPEED_AVG,
        SpeedRecord.SPEED_MIN,
        SpeedRecord.SPEED_MAX,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putMap("SPEED_AVG", velocityToJsMap(record[SpeedRecord.SPEED_AVG]))
      putMap("SPEED_MAX", velocityToJsMap(record[SpeedRecord.SPEED_MAX]))
      putMap("SPEED_MIN", velocityToJsMap(record[SpeedRecord.SPEED_MIN]))
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
