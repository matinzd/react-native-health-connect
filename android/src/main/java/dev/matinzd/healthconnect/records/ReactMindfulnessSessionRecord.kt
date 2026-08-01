@file:OptIn(androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi::class)

package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactMindfulnessSessionRecord : ReactHealthRecordImpl<MindfulnessSessionRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<MindfulnessSessionRecord> {
    return records.toMapList().map {
      MindfulnessSessionRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        mindfulnessSessionType = it.getSafeInt(
          "mindfulnessSessionType", MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_UNKNOWN
        ),
        title = it.getString("title"),
        notes = it.getString("notes"),
        metadata = convertMetadataFromJSMap(it.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: MindfulnessSessionRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putMap("startZoneOffset", zoneOffsetToJsMap(record.startZoneOffset))
      putString("endTime", record.endTime.toString())
      putMap("endZoneOffset", zoneOffsetToJsMap(record.endZoneOffset))
      putInt("mindfulnessSessionType", record.mindfulnessSessionType)
      putString("title", record.title)
      putString("notes", record.notes)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    throw AggregationNotSupported()
  }

  override fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResultGroupedByPeriod(record: List<AggregationResultGroupedByPeriod>): WritableNativeArray {
    throw AggregationNotSupported()
  }
}
