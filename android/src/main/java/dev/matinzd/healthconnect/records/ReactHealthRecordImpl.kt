package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray

interface ReactHealthRecordImpl {
  fun parseWriteRecord(readableArray: ReadableArray): List<Record>
  fun parseReadResponse(response: ReadRecordsResponse<out Record>?): WritableNativeArray
  fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<out Record>
}
