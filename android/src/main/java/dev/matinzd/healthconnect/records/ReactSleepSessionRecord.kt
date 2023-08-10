package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactSleepSessionRecord : ReactHealthRecordImpl<SleepSessionRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<SleepSessionRecord> {
    return records.toMapList().map { map ->
      SleepSessionRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        stages = map.getArray("stages")?.toMapList()?.map { stageMap ->
          SleepSessionRecord.Stage(
            startTime = Instant.parse(stageMap.getString("startTime")),
            endTime = Instant.parse(stageMap.getString("endTime")),
            stage = stageMap.getSafeInt("stage", SleepSessionRecord.STAGE_TYPE_UNKNOWN),
          )
        } ?: emptyList(),
        startZoneOffset = null,
        endZoneOffset = null,
        title = map.getString("title"),
        notes = map.getString("description"),
      )
    }
  }

  override fun parseRecord(record: SleepSessionRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putString("title", record.title)
      putString("notes", record.notes)
      putArray("stages", WritableNativeArray().apply {
        record.stages.map {
          val map = WritableNativeMap()
          map.putString("startTime", it.startTime.toString())
          map.putString("endTime", it.endTime.toString())
          map.putDouble("stage", it.stage.toDouble())
          this.pushMap(map)
        }
      })
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        SleepSessionRecord.SLEEP_DURATION_TOTAL
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble(
        "SLEEP_DURATION_TOTAL",
        record[SleepSessionRecord.SLEEP_DURATION_TOTAL]?.seconds?.toDouble() ?: 0.0
      )
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
