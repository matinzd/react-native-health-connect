package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactHeightRecord : ReactHealthRecordImpl<HeightRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<HeightRecord> {
    return records.toMapList().map { map ->
      HeightRecord(
        time = Instant.parse(map.getString("time")),
        height = getLengthFromJsMap(map.getMap("height")),
        zoneOffset = null,
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out HeightRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putMap("height", lengthToJsMap(record.height))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<HeightRecord> {
    return convertReactRequestOptionsFromJS(HeightRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        HeightRecord.HEIGHT_AVG,
        HeightRecord.HEIGHT_MAX,
        HeightRecord.HEIGHT_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putMap("HEIGHT_AVG", lengthToJsMap(record[HeightRecord.HEIGHT_AVG]!!))
      putMap("HEIGHT_MIN", lengthToJsMap(record[HeightRecord.HEIGHT_MIN]!!))
      putMap("HEIGHT_MAX", lengthToJsMap(record[HeightRecord.HEIGHT_MAX]!!))
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
