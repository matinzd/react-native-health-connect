package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactLeanBodyMassRecord : ReactHealthRecordImpl<LeanBodyMassRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<LeanBodyMassRecord> {
    return records.toMapList().map { map ->
      LeanBodyMassRecord(
        time = Instant.parse(map.getString("time")),
        mass = getMassFromJsMap(map.getMap("mass")),
        zoneOffset = null,
      )
    }
  }

  override fun parseRecord(record: LeanBodyMassRecord): WritableNativeMap {
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
