package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.getSafeInt
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactCervicalMucusRecord : ReactHealthRecordImpl<CervicalMucusRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<CervicalMucusRecord> {
    return records.toMapList().map {
      CervicalMucusRecord(
        time = Instant.parse(it.getString("time")),
        appearance = it.getSafeInt("appearance", CervicalMucusRecord.APPEARANCE_UNKNOWN),
        sensation = it.getSafeInt("sensation", CervicalMucusRecord.SENSATION_UNKNOWN),
        zoneOffset = null,
      )
    }
  }

  override fun parseRecord(record: CervicalMucusRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putInt("appearance", record.appearance)
      putInt("sensation", record.sensation)
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
