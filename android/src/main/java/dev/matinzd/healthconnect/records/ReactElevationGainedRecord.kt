package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactElevationGainedRecord : ReactHealthRecordImpl<ElevationGainedRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<ElevationGainedRecord> {
    return records.toMapList().map {
      ElevationGainedRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        elevation = getLengthFromJsMap(it.getMap("elevation"))
      )
    }
  }

  override fun parseRecord(record: ElevationGainedRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putMap("elevation", lengthToJsMap(record.elevation))
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
