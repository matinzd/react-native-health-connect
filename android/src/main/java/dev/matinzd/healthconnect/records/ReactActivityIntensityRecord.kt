package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.ActivityIntensityRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.convertMetadataFromJSMap
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.getSafeInt
import dev.matinzd.healthconnect.utils.toMapList
import java.time.Instant

class ReactActivityIntensityRecord : ReactHealthRecordImpl<ActivityIntensityRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<ActivityIntensityRecord> {
    return records.toMapList().map { map ->
      ActivityIntensityRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        activityIntensityType = map.getSafeInt(
          "activityIntensityType",
          ActivityIntensityRecord.ACTIVITY_INTENSITY_TYPE_MODERATE
        ),
        metadata = convertMetadataFromJSMap(map.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: ActivityIntensityRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putInt("activityIntensityType", record.activityIntensityType)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest = unsupportedAggregate()

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest = unsupportedAggregate()

  override fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest = unsupportedAggregate()

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap = unsupportedAggregate()

  override fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray = unsupportedAggregate()

  override fun parseAggregationResultGroupedByPeriod(record: List<AggregationResultGroupedByPeriod>): WritableNativeArray = unsupportedAggregate()

  private fun unsupportedAggregate(): Nothing {
    throw UnsupportedOperationException("ActivityIntensity aggregate is not supported")
  }
}
