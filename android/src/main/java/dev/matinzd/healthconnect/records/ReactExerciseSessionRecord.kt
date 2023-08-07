package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.ExerciseLap
import androidx.health.connect.client.records.ExerciseRoute
import androidx.health.connect.client.records.ExerciseRouteResult
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactExerciseSessionRecord : ReactHealthRecordImpl<ExerciseSessionRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<ExerciseSessionRecord> {
    return records.toMapList().map {
      ExerciseSessionRecord(
        startTime = Instant.parse(it.getString("startTime")),
        endTime = Instant.parse(it.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        exerciseType = it.getSafeInt(
          "exerciseType", ExerciseSessionRecord.EXERCISE_TYPE_OTHER_WORKOUT
        ),
        notes = it.getString("notes"),
        title = it.getString("title"),
        laps = it.getArray("samples")?.toMapList()?.map { sample ->
          ExerciseLap(
            startTime = Instant.parse(sample.getString("startTime")),
            endTime = Instant.parse(sample.getString("endTime")),
            length = getLengthFromJsMap(sample.getMap("length")),
          )
        } ?: emptyList(),
        segments = it.getArray("samples")?.toMapList()?.map { sample ->
          ExerciseSegment(
            startTime = Instant.parse(sample.getString("startTime")),
            endTime = Instant.parse(sample.getString("endTime")),
            segmentType = sample.getSafeInt(
              "segmentType", ExerciseSegment.EXERCISE_SEGMENT_TYPE_UNKNOWN
            ),
            repetitions = sample.getSafeInt("repetitions", 0),
          )
        } ?: emptyList(),
        exerciseRoute = ExerciseRoute(
          route = it.getMap("exerciseRoute")?.getArray("route")?.toMapList()?.map { sample ->
            ExerciseRoute.Location(
              time = Instant.parse(sample.getString("time")),
              latitude = sample.getDouble("latitude"),
              longitude = sample.getDouble("longitude"),
              horizontalAccuracy = getLengthFromJsMap(sample.getMap("horizontalAccuracy")),
              verticalAccuracy = getLengthFromJsMap(sample.getMap("verticalAccuracy")),
              altitude = getLengthFromJsMap(sample.getMap("altitude")),
            )
          } ?: emptyList(),
        )
      )
    }
  }

  override fun parseRecord(record: ExerciseSessionRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putString("notes", record.notes)
      putString("title", record.title)
      putInt("exerciseType", record.exerciseType)
      putArray("laps", WritableNativeArray().apply {
        record.laps.map {
          val map = WritableNativeMap()
          map.putString("startTime", it.startTime.toString())
          map.putString("endTime", it.endTime.toString())
          map.putMap("length", lengthToJsMap(it.length))
          this.pushMap(map)
        }
      })
      putArray("segments", WritableNativeArray().apply {
        record.segments.map {
          val map = WritableNativeMap()
          map.putString("startTime", it.startTime.toString())
          map.putString("endTime", it.endTime.toString())
          map.putDouble("repetitions", it.repetitions.toDouble())
          map.putDouble("segmentType", it.segmentType.toDouble())
          this.pushMap(map)
        }
      })


      when(record.exerciseRouteResult) {
        is ExerciseRouteResult.Data -> {
          val exerciseRouteMap = WritableNativeMap()
          exerciseRouteMap.putArray("route", WritableNativeArray().apply {
            (record.exerciseRouteResult as ExerciseRouteResult.Data).exerciseRoute.route.map {
              val map = WritableNativeMap()
              map.putString("time", it.time.toString())
              map.putDouble("latitude", it.latitude)
              map.putDouble("longitude", it.longitude)
              map.putMap("horizontalAccuracy", lengthToJsMap(it.horizontalAccuracy))
              map.putMap("verticalAccuracy", lengthToJsMap(it.verticalAccuracy))
              map.putMap("altitude", lengthToJsMap(it.altitude))
              this.pushMap(map)
            }
          })
          putMap("exerciseRoute", exerciseRouteMap)
        }
        is ExerciseRouteResult.NoData -> {
          putMap("exerciseRoute", WritableNativeMap())
        }
        is ExerciseRouteResult.ConsentRequired -> {
          throw Exception("Consent required")
        }
        else -> {
          putMap("exerciseRoute", WritableNativeMap())
        }
      }

      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(ExerciseSessionRecord.EXERCISE_DURATION_TOTAL),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      val map = WritableNativeMap().apply {
        putDouble(
          "inSeconds",
          record[ExerciseSessionRecord.EXERCISE_DURATION_TOTAL]?.seconds?.toDouble() ?: 0.0
        )
      }
      putMap("EXERCISE_DURATION_TOTAL", map)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
