package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactElevationGainedRecord : ReactHealthRecordImpl<ElevationGainedRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<ElevationGainedRecord> {
    return records.toMapList().map {
      ElevationGainedRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        elevation = getLengthFromJsMap(it.getMap("elevation"))
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out ElevationGainedRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("startTime", record.startTime.toString())
          putString("endTime", record.endTime.toString())
          putMap("elevation", lengthToJsMap(record.elevation))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<ElevationGainedRecord> {
    return convertReactRequestOptionsFromJS(ElevationGainedRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }
}
