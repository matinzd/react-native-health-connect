package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.convertReactRequestOptionsFromJS

class ReactBloodGlucoseHealthRecord : ReactHealthRecordImpl {
  override fun parseWriteRecord(readableArray: ReadableArray): List<Record> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out Record>?): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<BloodGlucoseRecord> {
    return convertReactRequestOptionsFromJS(BloodGlucoseRecord::class, readableMap)
  }
}
