package dev.matinzd.healthconnect

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import java.time.Instant
import kotlin.reflect.KClass

fun <T : Record> convertReactRequestOptionsFromJS(
  recordType: KClass<T>,
  readableMap: ReadableMap
): ReadRecordsRequest<T> {
  return ReadRecordsRequest(
    recordType,
    timeRangeFilter = TimeRangeFilter.between(
      Instant.parse(readableMap.getString("startTime")),
      Instant.parse(readableMap.getString("endTime")),
    ),
    dataOriginFilter = readableMap.getArray("dataOriginFilter")?.toArrayList()
      ?.mapNotNull { DataOrigin(it.toString()) }?.toSet() ?: emptySet(),
    ascendingOrder = if (readableMap.hasKey("ascendingOrder")) readableMap.getBoolean("ascendingOrder") else true,
    pageSize = if (readableMap.hasKey("pageSize")) readableMap.getInt("pageSize") else 1000,
    pageToken = if (readableMap.hasKey("pageToken")) readableMap.getString("pageToken") else null,
  )
}

fun convertProviderPackageNamesFromJS(providerPackageNames: ReadableArray): List<String> {
  return providerPackageNames.toArrayList().map { it.toString() }.toList()
}
