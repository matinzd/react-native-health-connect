package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.Percentage
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
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

  override fun parseRecord(record: BodyFatRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("percentage", record.percentage.value)
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
