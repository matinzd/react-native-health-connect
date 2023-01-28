package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap

class ReactHeightRecord : ReactHealthRecordImpl<HeightRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<HeightRecord> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out HeightRecord>): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<HeightRecord> {
    TODO("Not yet implemented")
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    TODO("Not yet implemented")
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    TODO("Not yet implemented")
  }
}
