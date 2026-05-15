package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Device
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.*
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import java.time.*
import kotlin.reflect.KClass
import kotlin.text.toLong

fun <T : Record> convertReactRequestOptionsFromJS(
  recordType: KClass<T>, options: ReadableMap
): ReadRecordsRequest<T> {
  // Move to refined version
  // inline fun <reified T : Record> ReadRecordsRequest()
  // link: https://android-review.googlesource.com/#/q/If58a5c2c9acea1c22b322537daa4fa513065e393
  return ReadRecordsRequest(
    recordType,
    timeRangeFilter = options.getTimeRangeFilter("timeRangeFilter"),
    dataOriginFilter = convertJsToDataOriginSet(options.getArray("dataOriginFilter")),
    ascendingOrder = options.getSafeBoolean("ascendingOrder", true),
    pageSize = options.getSafeInt("pageSize", 1000),
    pageToken = if (options.hasKey("pageToken")) options.getString("pageToken") else null,
  )
}

fun convertDataOriginsToJsArray(dataOrigin: Set<DataOrigin>): WritableNativeArray {
  return WritableNativeArray().apply {
    dataOrigin.forEach {
      pushString(it.packageName)
    }
  }
}

fun convertJsToDataOriginSet(readableArray: ReadableArray?): Set<DataOrigin> {
  if (readableArray == null) {
    return emptySet()
  }

  return readableArray.toArrayList().mapNotNull { DataOrigin(it.toString()) }.toSet()
}

fun convertJsToRecordTypeSet(readableArray: ReadableArray?): Set<KClass<out Record>> {
  if (readableArray == null) {
    return emptySet()
  }

  return readableArray.toArrayList().mapNotNull { reactRecordTypeToClassMap[it.toString()] }.toSet()
}

fun ReadableArray.toMapList(): List<ReadableMap> {
  val list = mutableListOf<ReadableMap>()
  for (i in 0 until size()) {
    val item = getMap(i)
    if (null != item) {
      list.add(item)
    }
  }
  return list
}

fun ReadableMap.getSafeInt(key: String, default: Int): Int {
  return if (this.hasKey(key)) this.getInt(key) else default
}

fun ReadableMap.getSafeBoolean(key: String, default: Boolean): Boolean {
  return if (this.hasKey(key)) this.getBoolean(key) else default
}

fun ReadableMap.getSafeString(key: String, default: String): String {
  return if (this.hasKey(key)) this.getString(key) ?: default else default
}

fun ReadableMap.getSafeDouble(key: String, default: Double): Double {
  return if (this.hasKey(key)) this.getDouble(key) else default
}

fun ReadableMap.getTimeRangeFilter(key: String? = null): TimeRangeFilter {
  val timeRangeFilter = if (key != null) this.getMap(key)
    ?: throw Exception("Time range filter should be provided") else this

  val operator = timeRangeFilter.getString("operator")

  val startTime =
    if (timeRangeFilter.hasKey("startTime")) Instant.parse(timeRangeFilter.getString("startTime")) else null

  val endTime =
    if (timeRangeFilter.hasKey("endTime")) Instant.parse(timeRangeFilter.getString("endTime")) else null

  when (operator) {
    "between" -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
    "after" -> {
      if (startTime == null) {
        throw Exception("Start time should be provided")
      }

      return TimeRangeFilter.after(startTime)
    }
    "before" -> {
      if (endTime == null) {
        throw Exception("End time should be provided")
      }

      return TimeRangeFilter.before(endTime)
    }
    else -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
  }
}

fun ReadableMap.getTimeRangeFilterLocal(key: String? = null): TimeRangeFilter {
  val timeRangeFilter = if (key != null) this.getMap(key)
    ?: throw Exception("Time range filter should be provided") else this

  val operator = timeRangeFilter.getString("operator")
  val zone = ZoneId.systemDefault()

  val startTime: LocalDateTime? =
    if (timeRangeFilter.hasKey("startTime")) Instant.parse(timeRangeFilter.getString("startTime")).atZone(zone).toLocalDateTime() else null

  val endTime: LocalDateTime? =
    if (timeRangeFilter.hasKey("endTime")) Instant.parse(timeRangeFilter.getString("endTime")).atZone(zone).toLocalDateTime() else null

  when (operator) {
    "between" -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
    "after" -> {
      if (startTime == null) {
        throw Exception("Start time should be provided")
      }

      return TimeRangeFilter.after(startTime)
    }
    "before" -> {
      if (endTime == null) {
        throw Exception("End time should be provided")
      }

      return TimeRangeFilter.before(endTime)
    }
    else -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
  }
}

fun convertMetadataFromJSMap(meta: ReadableMap?): Metadata {
  if (meta == null) {
    return Metadata()
  }

  return Metadata(
    id = meta.getSafeString("id", ""),
    clientRecordId = meta.getString("clientRecordId"),
    clientRecordVersion = meta.getSafeDouble("clientRecordVersion", 0.0).toLong(),
    dataOrigin = DataOrigin(meta.getSafeString("dataOrigin", "")),
    lastModifiedTime = Instant.parse(meta.getSafeString("lastModifiedTime", Instant.now().toString())),
    device = meta.getMap("device")?.let {
      Device(
        type = it.getSafeInt("type", Device.TYPE_UNKNOWN),
        manufacturer = it.getString("manufacturer"),
        model = it.getString("model"),
      )
    },
    recordingMethod = meta.getSafeInt("recordingMethod", Metadata.RECORDING_METHOD_UNKNOWN)
  )
}

fun convertMetadataToJSMap(meta: Metadata): WritableNativeMap {
  return WritableNativeMap().apply {
    putString("id", meta.id)
    putString("clientRecordId", meta.clientRecordId)
    putDouble("clientRecordVersion", meta.clientRecordVersion.toDouble())
    putString("dataOrigin", meta.dataOrigin.packageName)
    putString("lastModifiedTime", meta.lastModifiedTime.toString())
    putMap("device", convertDeviceToJSMap(meta.device))
    putInt("recordingMethod", meta.recordingMethod)
    putString("manufacturer", meta.device?.manufacturer)
    putString("model", meta.device?.model)
  }
}

fun convertDeviceToJSMap(device: Device?): WritableNativeMap? {
  if (device == null) {
    return null
  }

  return WritableNativeMap().apply {
    putInt("type", device.type)
    putString("manufacturer", device.manufacturer)
    putString("model", device.model)
  }
}

fun massToJsMap(mass: Mass?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inGrams", mass?.inGrams ?: 0.0)
    putDouble("inKilograms", mass?.inKilograms ?: 0.0)
    putDouble("inMilligrams", mass?.inMilligrams ?: 0.0)
    putDouble("inMicrograms", mass?.inMicrograms ?: 0.0)
    putDouble("inOunces", mass?.inOunces ?: 0.0)
    putDouble("inPounds", mass?.inPounds ?: 0.0)
  }
}

fun getMassFromJsMap(massMap: ReadableMap?): Mass {
  if (massMap == null) {
    throw InvalidMass()
  }

  val value = massMap.getDouble("value")
  return when (massMap.getString("unit")) {
    "grams" -> Mass.grams(value)
    "kilograms" -> Mass.kilograms(value)
    "milligrams" -> Mass.milligrams(value)
    "micrograms" -> Mass.micrograms(value)
    "ounces" -> Mass.ounces(value)
    "pounds" -> Mass.pounds(value)
    else -> Mass.grams(value)
  }
}

fun getLengthFromJsMap(length: ReadableMap?): Length {
  if (length == null) {
    throw InvalidLength()
  }

  val value = length.getDouble("value")
  return when (length.getString("unit")) {
    "meters" -> Length.meters(value)
    "kilometers" -> Length.kilometers(value)
    "miles" -> Length.miles(value)
    "inches" -> Length.inches(value)
    "feet" -> Length.feet(value)
    else -> Length.meters(value)
  }
}

fun lengthToJsMap(length: Length?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inMeters", length?.inMeters ?: 0.0)
    putDouble("inKilometers", length?.inKilometers ?: 0.0)
    putDouble("inMiles", length?.inMiles ?: 0.0)
    putDouble("inInches", length?.inInches ?: 0.0)
    putDouble("inFeet", length?.inFeet ?: 0.0)
  }
}

fun zoneOffsetToJsMap(zoneOffset: ZoneOffset?): WritableNativeMap? {
  if (zoneOffset == null) {
    return null
  }

  return WritableNativeMap().apply {
    putString("id", zoneOffset.id)
    putInt("totalSeconds", zoneOffset.totalSeconds)
  }
}

fun getVolumeFromJsMap(volume: ReadableMap?): Volume {
  if (volume == null) {
    throw InvalidLength()
  }

  val value = volume.getDouble("value")
  return when (volume.getString("unit")) {
    "fluidOuncesUs" -> Volume.fluidOuncesUs(value)
    "liters" -> Volume.liters(value)
    "milliliters" -> Volume.milliliters(value)
    else -> Volume.liters(value)
  }
}

fun volumeToJsMap(volume: Volume?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inLiters", volume?.inLiters ?: 0.0)
    putDouble("inFluidOuncesUs", volume?.inFluidOuncesUs ?: 0.0)
    putDouble("inMilliliters", volume?.inMilliliters ?: 0.0)
  }
}

fun getEnergyFromJsMap(energyMap: ReadableMap?): Energy {
  if (energyMap == null) {
    throw InvalidEnergy()
  }

  val value = energyMap.getDouble("value")
  return when (energyMap.getString("unit")) {
    "kilojoules" -> Energy.kilojoules(value)
    "kilocalories" -> Energy.kilocalories(value)
    "joules" -> Energy.joules(value)
    "calories" -> Energy.calories(value)
    else -> Energy.calories(value)
  }
}

fun energyToJsMap(energy: Energy?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inCalories", energy?.inCalories ?: 0.0)
    putDouble("inJoules", energy?.inJoules ?: 0.0)
    putDouble("inKilocalories", energy?.inKilocalories ?: 0.0)
    putDouble("inKilojoules", energy?.inKilojoules ?: 0.0)
  }
}

fun getVelocityFromJsMap(velocityMap: ReadableMap?): Velocity {
  if (velocityMap == null) {
    throw InvalidVelocity()
  }

  val value = velocityMap.getDouble("value")
  return when (velocityMap.getString("unit")) {
    "kilometersPerHour" -> Velocity.kilometersPerHour(value)
    "metersPerSecond" -> Velocity.metersPerSecond(value)
    "milesPerHour" -> Velocity.milesPerHour(value)
    else -> Velocity.kilometersPerHour(value)
  }
}

fun velocityToJsMap(velocity: Velocity?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inKilometersPerHour", velocity?.inKilometersPerHour ?: 0.0)
    putDouble("inMetersPerSecond", velocity?.inMetersPerSecond ?: 0.0)
    putDouble("inMilesPerHour", velocity?.inMilesPerHour ?: 0.0)
  }
}

fun getPowerFromJsMap(powerMap: ReadableMap?): Power {
  if (powerMap == null) {
    throw InvalidVelocity()
  }

  val value = powerMap.getDouble("value")
  return when (powerMap.getString("unit")) {
    "watts" -> Power.watts(value)
    "kilocaloriesPerDay" -> Power.kilocaloriesPerDay(value)
    else -> Power.kilocaloriesPerDay(value)
  }
}

fun powerToJsMap(power: Power?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inWatts", power?.inWatts ?: 0.0)
    putDouble("inKilocaloriesPerDay", power?.inKilocaloriesPerDay ?: 0.0)
  }
}

fun convertChangesTokenRequestOptionsFromJS(options: ReadableMap): ChangesTokenRequest {
  return ChangesTokenRequest(
    recordTypes = convertJsToRecordTypeSet(options.getArray("recordTypes")),
    dataOriginFilters = convertJsToDataOriginSet(options.getArray("dataOriginFilters")),
  )
}

fun mapJsDurationToDuration(duration: ReadableMap?): Duration {
  if (duration == null) {
    return Duration.ofDays(0)
  }
  val length = duration.getInt("length").toLong()
  return when (duration.getString("duration")) {
    "MILLIS" -> Duration.ofMillis(length)
    "SECONDS" -> Duration.ofSeconds(length)
    "MINUTES" -> Duration.ofMinutes(length)
    "HOURS" -> Duration.ofHours(length)
    "DAYS" -> Duration.ofDays(length)
    else -> Duration.ofDays(length)
  }
}

fun mapJsPeriodToPeriod(period: ReadableMap?): Period {
  if (period == null) {
    return Period.ofDays(0)
  }
  val length = period.getInt("length")
  return when (period.getString("period")) {
    "DAYS" -> Period.ofDays(length)
    "WEEKS" -> Period.ofWeeks(length)
    "MONTHS" -> Period.ofMonths(length)
    "YEARS" -> Period.ofYears(length)
    else -> Period.ofDays(length)
  }
}
