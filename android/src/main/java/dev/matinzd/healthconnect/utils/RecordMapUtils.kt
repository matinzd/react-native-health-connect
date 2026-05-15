package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.records.metadata.Metadata
import com.facebook.react.bridge.WritableNativeMap
import java.time.Instant
import java.time.ZoneOffset

fun WritableNativeMap.putIntervalRecordTime(
  startTime: Instant,
  endTime: Instant,
  startZoneOffset: ZoneOffset?,
  endZoneOffset: ZoneOffset?,
) {
  putString("startTime", startTime.toString())
  putString("endTime", endTime.toString())
  putMap("startZoneOffset", zoneOffsetToJsMap(startZoneOffset))
  putMap("endZoneOffset", zoneOffsetToJsMap(endZoneOffset))
}

fun WritableNativeMap.putInstantRecordTime(
  time: Instant,
  zoneOffset: ZoneOffset?,
) {
  putString("time", time.toString())
  putMap("zoneOffset", zoneOffsetToJsMap(zoneOffset))
}

fun WritableNativeMap.putTime(time: Instant) {
  putString("time", time.toString())
}

fun WritableNativeMap.putMetadata(meta: Metadata) {
  putMap("metadata", convertMetadataToJSMap(meta))
}
