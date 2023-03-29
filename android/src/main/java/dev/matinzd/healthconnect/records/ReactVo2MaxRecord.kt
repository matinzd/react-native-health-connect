package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.getSafeInt
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactVo2MaxRecord : ReactHealthRecordImpl<Vo2MaxRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<Vo2MaxRecord> {
    return records.toMapList().map { map ->
      Vo2MaxRecord(
        time = Instant.parse(map.getString("time")),
        zoneOffset = null,
        vo2MillilitersPerMinuteKilogram = map.getDouble("vo2Max"),
        measurementMethod = map.getSafeInt(
          "measurementMethod",
          Vo2MaxRecord.MEASUREMENT_METHOD_OTHER
        ),
      )
    }
  }

  override fun parseRecord(record: Vo2MaxRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("vo2MillilitersPerMinuteKilogram", record.vo2MillilitersPerMinuteKilogram)
      putInt("measurementMethod", record.measurementMethod)
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
