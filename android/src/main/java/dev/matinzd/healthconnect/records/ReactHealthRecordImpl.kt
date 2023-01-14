package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray

interface ReactHealthRecordImpl<T: Record> {
  fun parseWriteRecord(records: ReadableArray): List<T>
  fun parseReadResponse(response: ReadRecordsResponse<out T>): WritableNativeArray
  fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<T>
}
