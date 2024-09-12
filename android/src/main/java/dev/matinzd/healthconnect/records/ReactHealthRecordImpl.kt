package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.bridge.WritableNativeArray

interface ReactHealthRecordImpl<T : Record> {
  fun parseWriteRecord(records: ReadableArray): List<T>
  fun parseRecord(record: T): WritableNativeMap
  fun getAggregateRequest(record: ReadableMap): AggregateRequest
  fun parseAggregationResult(record: AggregationResult): WritableNativeMap

  fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest ? = null
  fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray ? = null
}
