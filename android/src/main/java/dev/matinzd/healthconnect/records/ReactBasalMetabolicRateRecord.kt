package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS

class ReactBasalMetabolicRateRecord : ReactHealthRecordImpl<BasalMetabolicRateRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BasalMetabolicRateRecord> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BasalMetabolicRateRecord>): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<BasalMetabolicRateRecord> {
    return convertReactRequestOptionsFromJS(BasalMetabolicRateRecord::class, readableMap)
  }
}
