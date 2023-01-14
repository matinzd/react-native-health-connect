package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS

class ReactBloodGlucoseRecord : ReactHealthRecordImpl<BloodGlucoseRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BloodGlucoseRecord> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BloodGlucoseRecord>): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(options: ReadableMap): ReadRecordsRequest<BloodGlucoseRecord> {
    return convertReactRequestOptionsFromJS(BloodGlucoseRecord::class, options)
  }
}
