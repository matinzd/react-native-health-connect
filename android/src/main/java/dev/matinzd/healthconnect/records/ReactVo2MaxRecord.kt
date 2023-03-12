package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap

class ReactVo2MaxRecord : ReactHealthRecordImpl<Vo2MaxRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<Vo2MaxRecord> {
    TODO("Not yet implemented")
  }

  override fun parseRecord(record: Vo2MaxRecord): WritableNativeMap {
    TODO("Not yet implemented")
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    TODO("Not yet implemented")
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    TODO("Not yet implemented")
  }
}
