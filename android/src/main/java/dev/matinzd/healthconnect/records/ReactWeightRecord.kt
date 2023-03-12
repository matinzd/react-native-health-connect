package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactWeightRecord : ReactHealthRecordImpl<WeightRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<WeightRecord> {
    return records.toMapList().map { map ->
      WeightRecord(
        time = Instant.parse(map.getString("time")),
        weight = getMassFromJsMap(map.getMap("weight")),
        zoneOffset = null
      )
    }
  }

  override fun parseRecord(record: WeightRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putMap("weight", massToJsMap(record.weight))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        WeightRecord.WEIGHT_AVG,
        WeightRecord.WEIGHT_MAX,
        WeightRecord.WEIGHT_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
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
}
