package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.ExerciseLap
import androidx.health.connect.client.records.ExerciseRoute
import androidx.health.connect.client.records.ExerciseRouteResult
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableNativeArray
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactExerciseSessionRecord : ReactHealthRecordImpl<ExerciseSessionRecord> {
  private val aggregateMetrics = setOf(ExerciseSessionRecord.EXERCISE_DURATION_TOTAL)

  override fun parseWriteRecord(records: ReadableArray): List<ExerciseSessionRecord> {
    return records.toMapList().map {
      val routeList = it.getMap("exerciseRoute")?.getArray("route")?.toMapList()?.map { sample ->
        ExerciseRoute.Location(
          time = Instant.parse(sample.getString("time")),
          latitude = sample.getDouble("latitude"),
          longitude = sample.getDouble("longitude"),
          horizontalAccuracy = getLengthFromJsMap(sample.getMap("horizontalAccuracy")),
          verticalAccuracy = getLengthFromJsMap(sample.getMap("verticalAccuracy")),
          altitude = getLengthFromJsMap(sample.getMap("altitude")),
        )
      } ?: emptyList()

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
        exerciseRoute = if (routeList.isNotEmpty()) {
          ExerciseRoute(
            route = routeList,
          )
        } else {
          null
        },
        metadata = convertMetadataFromJSMap(it.getMap("metadata"))
      )
    }
  }

  override fun parseRecord(record: ExerciseSessionRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putMap("startZoneOffset", zoneOffsetToJsMap(record.startZoneOffset))
      putString("endTime", record.endTime.toString())
      putMap("endZoneOffset", zoneOffsetToJsMap(record.endZoneOffset))
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


      val exerciseRouteMap = WritableNativeMap()
      when (record.exerciseRouteResult) {
        is ExerciseRouteResult.Data -> {
          val exerciseRoute: ExerciseRoute =
            (record.exerciseRouteResult as ExerciseRouteResult.Data).exerciseRoute
          val route = parseExerciseRoute(exerciseRoute)
          exerciseRouteMap.putString("type", "DATA")
          exerciseRouteMap.putArray("route", route)
        }
        is ExerciseRouteResult.NoData -> {
          exerciseRouteMap.putString("type", "NO_DATA")
          exerciseRouteMap.putArray("route", WritableNativeArray())

        }
        is ExerciseRouteResult.ConsentRequired -> {
          exerciseRouteMap.putString("type", "CONSENT_REQUIRED")
          exerciseRouteMap.putArray("route", WritableNativeArray())
        }
      }
      putMap("exerciseRoute", exerciseRouteMap)

      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = aggregateMetrics,
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    return AggregateGroupByDurationRequest(
      metrics = aggregateMetrics,
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      timeRangeSlicer = mapJsDurationToDuration(record.getMap("timeRangeSlicer")),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest {
    return AggregateGroupByPeriodRequest(
      metrics = aggregateMetrics,
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      timeRangeSlicer = mapJsPeriodToPeriod(record.getMap("timeRangeSlicer")),
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

  override fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray {
    return WritableNativeArray().apply {
      record.forEach {
        val map = WritableNativeMap().apply {
          putMap("result", parseAggregationResult(it.result))
          putString("startTime", it.startTime.toString())
          putString("endTime", it.endTime.toString())
          putString("zoneOffset", it.zoneOffset.toString())
        }
        pushMap(map)
      }
    }
  }

  override fun parseAggregationResultGroupedByPeriod(record: List<AggregationResultGroupedByPeriod>): WritableNativeArray {
    return WritableNativeArray().apply {
      record.forEach {
        val map = WritableNativeMap().apply {
          putMap("result", parseAggregationResult(it.result))
          putString("startTime", it.startTime.toString())
          putString("endTime", it.endTime.toString())
        }
        pushMap(map)
      }
    }
  }

  companion object {
    fun parseExerciseRoute(exerciseRoute: ExerciseRoute): ReadableNativeArray {
      return WritableNativeArray().apply {
          exerciseRoute.route.map {
            val map = WritableNativeMap()
            map.putString("time", it.time.toString())
            map.putDouble("latitude", it.latitude)
            map.putDouble("longitude", it.longitude)
            map.putMap("horizontalAccuracy", lengthToJsMap(it.horizontalAccuracy))
            map.putMap("verticalAccuracy", lengthToJsMap(it.verticalAccuracy))
            map.putMap("altitude", lengthToJsMap(it.altitude))
            this.pushMap(map)
        }
      }
    }
  }
}
