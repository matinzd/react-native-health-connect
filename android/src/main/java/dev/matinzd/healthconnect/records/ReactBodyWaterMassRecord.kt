package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
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

  override fun parseRecord(record: BodyWaterMassRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putMap("mass", massToJsMap(record.mass))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }
}
