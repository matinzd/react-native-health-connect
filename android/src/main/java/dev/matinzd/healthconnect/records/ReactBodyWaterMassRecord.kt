package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactBodyWaterMassRecord : ReactHealthRecordImpl<BodyWaterMassRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BodyWaterMassRecord> {
    return records.toMapList().map {
      BodyWaterMassRecord(
        time = Instant.parse(it.getString("time")),
        mass = getMassFromJsMap(it.getMap("mass")),
        zoneOffset = null,
      )
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BodyWaterMassRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putMap("mass", massToJsMap(record.mass))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<BodyWaterMassRecord> {
    return convertReactRequestOptionsFromJS(BodyWaterMassRecord::class, options)
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }
}
