package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.aggregate.AggregateMetric
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap

fun createAggregateRequest(
  record: ReadableMap,
  aggregateMetrics: Set<AggregateMetric<*>>,
  useLocalTimeRange: Boolean = false,
): AggregateRequest {
  return AggregateRequest(
    metrics = aggregateMetrics,
    timeRangeFilter = if (useLocalTimeRange) {
      record.getTimeRangeFilterLocal("timeRangeFilter")
    } else {
      record.getTimeRangeFilter("timeRangeFilter")
    },
    dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
  )
}

fun createAggregateGroupByDurationRequest(
  record: ReadableMap,
  aggregateMetrics: Set<AggregateMetric<*>>,
  useLocalTimeRange: Boolean = false,
): AggregateGroupByDurationRequest {
  return AggregateGroupByDurationRequest(
    metrics = aggregateMetrics,
    timeRangeFilter = if (useLocalTimeRange) {
      record.getTimeRangeFilterLocal("timeRangeFilter")
    } else {
      record.getTimeRangeFilter("timeRangeFilter")
    },
    timeRangeSlicer = mapJsDurationToDuration(record.getMap("timeRangeSlicer")),
    dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
  )
}

fun createAggregateGroupByPeriodRequest(
  record: ReadableMap,
  aggregateMetrics: Set<AggregateMetric<*>>,
  useLocalTimeRange: Boolean = false,
): AggregateGroupByPeriodRequest {
  return AggregateGroupByPeriodRequest(
    metrics = aggregateMetrics,
    timeRangeFilter = if (useLocalTimeRange) {
      record.getTimeRangeFilterLocal("timeRangeFilter")
    } else {
      record.getTimeRangeFilter("timeRangeFilter")
    },
    timeRangeSlicer = mapJsPeriodToPeriod(record.getMap("timeRangeSlicer")),
    dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
  )
}
