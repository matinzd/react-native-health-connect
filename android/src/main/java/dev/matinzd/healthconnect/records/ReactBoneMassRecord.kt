package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactBoneMassRecord : ReactHealthRecordImpl<BoneMassRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BoneMassRecord> {
    return records.toMapList().map {
      BoneMassRecord(
        time = Instant.parse(it.getString("time")),
        mass = getMassFromJsMap(it.getMap("mass")),
        zoneOffset = null,
      )
    }
  }

  override fun parseRecord(record: BoneMassRecord): WritableNativeMap {
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
