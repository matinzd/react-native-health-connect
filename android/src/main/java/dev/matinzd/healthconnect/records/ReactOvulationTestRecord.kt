package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.getSafeInt
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactOvulationTestRecord : ReactHealthRecordImpl<OvulationTestRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<OvulationTestRecord> {
    return records.toMapList().map { map ->
      OvulationTestRecord(
        time = Instant.parse(map.getString("time")), zoneOffset = null, result = map.getSafeInt(
          "result", OvulationTestRecord.RESULT_INCONCLUSIVE
        )
      )
    }
  }

  override fun parseRecord(record: OvulationTestRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putInt("result", record.result)
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
