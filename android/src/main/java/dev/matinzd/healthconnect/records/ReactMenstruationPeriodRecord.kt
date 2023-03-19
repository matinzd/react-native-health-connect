package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactMenstruationPeriodRecord : ReactHealthRecordImpl<MenstruationPeriodRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<MenstruationPeriodRecord> {
    return records.toMapList().map { map ->
      MenstruationPeriodRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        endZoneOffset = null,
        startZoneOffset = null
      )
    }
  }

  override fun parseRecord(record: MenstruationPeriodRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
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
