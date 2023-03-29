package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactPowerRecord : ReactHealthRecordImpl<PowerRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<PowerRecord> {
    return records.toMapList().map { map ->
      PowerRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        samples = map.getArray("samples")?.toMapList()?.map { sample ->
          PowerRecord.Sample(
            time = Instant.parse(sample.getString("time")),
            power = getPowerFromJsMap(sample.getMap("power"))
          )
        } ?: emptyList(),
      )
    }
  }

  override fun parseRecord(record: PowerRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      val array = WritableNativeArray().apply {
        record.samples.map {
          val map = WritableNativeMap()
          map.putString("time", it.time.toString())
          map.putMap("power", powerToJsMap(it.power))
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
        PowerRecord.POWER_AVG,
        PowerRecord.POWER_MAX,
        PowerRecord.POWER_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putMap("POWER_AVG", powerToJsMap(record[PowerRecord.POWER_AVG]))
      putMap("POWER_MAX", powerToJsMap(record[PowerRecord.POWER_MAX]))
      putMap("POWER_MIN", powerToJsMap(record[PowerRecord.POWER_MIN]))
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
