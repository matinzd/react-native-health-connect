package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.getSafeInt
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactMenstruationFlowRecord : ReactHealthRecordImpl<MenstruationFlowRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<MenstruationFlowRecord> {
    return records.toMapList().map { map ->
      MenstruationFlowRecord(
        time = Instant.parse(map.getString("time")),
        flow = map.getSafeInt("flow", MenstruationFlowRecord.FLOW_UNKNOWN),
        zoneOffset = null,
      )
    }
  }

  override fun parseRecord(record: MenstruationFlowRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putInt("flow", record.flow)
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
