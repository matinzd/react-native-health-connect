package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactStepsCadenceRecord : ReactHealthRecordImpl<StepsCadenceRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<StepsCadenceRecord> {
    return records.toMapList().map { map ->
      StepsCadenceRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        samples = map.getArray("samples")?.toMapList()?.map { sample ->
          StepsCadenceRecord.Sample(
            time = Instant.parse(sample.getString("time")),
            rate = sample.getDouble("count")
          )
        } ?: emptyList()
      )
    }
  }

  override fun parseRecord(record: StepsCadenceRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      val array = WritableNativeArray().apply {
        record.samples.map {
          val map = WritableNativeMap()
          map.putString("time", it.time.toString())
          map.putDouble("rate", it.rate)
          this.pushMap(map)
        }
      }
      putArray("samples", array)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        StepsCadenceRecord.RATE_AVG,
        StepsCadenceRecord.RATE_MAX,
        StepsCadenceRecord.RATE_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("RATE_AVG", record[StepsCadenceRecord.RATE_AVG]?.toDouble() ?: 0.0)
      putDouble("RATE_MAX", record[StepsCadenceRecord.RATE_MAX]?.toDouble() ?: 0.0)
      putDouble("RATE_MIN", record[StepsCadenceRecord.RATE_MIN]?.toDouble() ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
