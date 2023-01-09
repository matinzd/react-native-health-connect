package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import java.time.Instant

class ReactBasalMetabolicRateHealthRecord : ReactHealthRecordImpl {
  override fun parseWriteRecord(readableArray: ReadableArray): List<Record> {
    TODO("Not yet implemented")
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out Record>?): WritableNativeArray {
    TODO("Not yet implemented")
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<BasalMetabolicRateRecord> {
    return ReadRecordsRequest(
      BasalMetabolicRateRecord::class,
      timeRangeFilter = TimeRangeFilter.between(
        Instant.parse(readableMap.getString("startTime")),
        Instant.parse(readableMap.getString("endTime")),
      ),
      dataOriginFilter = readableMap.getArray("dataOriginFilter")?.toArrayList()
        ?.mapNotNull { DataOrigin(it.toString()) }?.toSet() ?: emptySet(),
      ascendingOrder = readableMap.getBoolean("ascendingOrder"),
      pageSize = readableMap.getInt("pageSize"),
      pageToken = readableMap.getString("pageToken")
    )
  }
}
