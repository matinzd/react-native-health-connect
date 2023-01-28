package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.units.Percentage
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactBodyFatRecord : ReactHealthRecordImpl<BodyFatRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BodyFatRecord> {
    return records.toMapList().map {
      BodyFatRecord(
        time = Instant.parse(it.getString("time")),
        percentage = Percentage(it.getDouble("percentage")),
        zoneOffset = null
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BodyFatRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putDouble("percentage", record.percentage.value)
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<BodyFatRecord> {
    return convertReactRequestOptionsFromJS(BodyFatRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }
}
