package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.HealthConnectUtils
import java.time.Instant

class ReactBasalBodyTemperatureHealthRecord : ReactHealthRecordImpl {
  override fun parseWriteRecord(readableArray: ReadableArray): List<Record> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out Record>?): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<BasalBodyTemperatureRecord> {
    return HealthConnectUtils.convertReactRequestOptionsFromJS(BasalBodyTemperatureRecord::class, readableMap)
  }
}
