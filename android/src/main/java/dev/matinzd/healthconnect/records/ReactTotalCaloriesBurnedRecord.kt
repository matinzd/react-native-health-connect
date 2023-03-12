package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap

class ReactTotalCaloriesBurnedRecord : ReactHealthRecordImpl<TotalCaloriesBurnedRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<TotalCaloriesBurnedRecord> {
    TODO("Not yet implemented")
  }

  override fun parseRecord(record: TotalCaloriesBurnedRecord): WritableNativeMap {
    TODO("Not yet implemented")
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    TODO("Not yet implemented")
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    TODO("Not yet implemented")
  }
}
