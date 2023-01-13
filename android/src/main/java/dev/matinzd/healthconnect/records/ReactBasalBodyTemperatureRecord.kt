package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS

class ReactBasalBodyTemperatureRecord : ReactHealthRecordImpl<BasalBodyTemperatureRecord> {
  override fun parseWriteRecord(readableArray: ReadableArray): List<BasalBodyTemperatureRecord> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BasalBodyTemperatureRecord>): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<BasalBodyTemperatureRecord> {
    return convertReactRequestOptionsFromJS(BasalBodyTemperatureRecord::class, readableMap)
  }
}
