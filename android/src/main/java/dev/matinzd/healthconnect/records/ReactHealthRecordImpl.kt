package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap

interface ReactHealthRecordImpl<T : Record> {
  fun parseWriteRecord(records: ReadableArray): List<T>
  fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<T>
  fun parseRecord(record: T): WritableNativeMap
  fun getAggregateRequest(record: ReadableMap): AggregateRequest
  fun parseAggregationResult(record: AggregationResult): WritableNativeMap
}
