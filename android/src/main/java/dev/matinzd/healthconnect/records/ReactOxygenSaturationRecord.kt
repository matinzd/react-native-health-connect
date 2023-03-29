package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.Percentage
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactOxygenSaturationRecord : ReactHealthRecordImpl<OxygenSaturationRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<OxygenSaturationRecord> {
    return records.toMapList().map { map ->
      OxygenSaturationRecord(
        time = Instant.parse(map.getString("time")),
        zoneOffset = null,
        percentage = Percentage(map.getDouble("percentage")),
      )
    }
  }

  override fun parseRecord(record: OxygenSaturationRecord): WritableNativeMap {
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
