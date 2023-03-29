package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactRespiratoryRateRecord : ReactHealthRecordImpl<RespiratoryRateRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<RespiratoryRateRecord> {
    return records.toMapList().map { map ->
      RespiratoryRateRecord(
        time = Instant.parse(map.getString("time")), zoneOffset = null, rate = map.getDouble("rate")
      )
    }
  }

  override fun parseRecord(record: RespiratoryRateRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("rate", record.rate)
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
