package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.units.Temperature
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.InvalidTemperature
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS
import java.time.Instant

class ReactBasalBodyTemperatureRecord : ReactHealthRecordImpl<BasalBodyTemperatureRecord> {
  private fun getTemperatureFromJsMap(temperature: ReadableMap?): Temperature {
    if (temperature == null) {
      throw InvalidTemperature()
    }

    val value = temperature.getDouble("value")
    return when (temperature.getString("unit")) {
      "fahrenheit" -> Temperature.fahrenheit(value)
      "calories" -> Temperature.celsius(value)
      else -> Temperature.celsius(value)
    }
  }

  private fun temperatureToJsMap(temperature: Temperature): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inFahrenheit", temperature.inFahrenheit)
      putDouble("inCelsius", temperature.inCelsius)
    }
  }

  override fun parseWriteRecord(records: ReadableArray): List<BasalBodyTemperatureRecord> {
    return ArrayList<BasalBodyTemperatureRecord>().apply {
      for (i in 0 until records.size()) {
        val record = records.getMap(i)
        add(
          BasalBodyTemperatureRecord(
            time = Instant.parse(record.getString("time")),
            zoneOffset = null,
            temperature = getTemperatureFromJsMap(record.getMap("temperature")),
            measurementLocation = if (record.hasKey("measurementLocation")) record.getInt(
              "measurementLocation"
            ) else BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN
          )
        )
      }
    }
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out BasalBodyTemperatureRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("time", record.time.toString())
          putInt("measurementLocation", record.measurementLocation)
          putMap("temperature", temperatureToJsMap(record.temperature))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<BasalBodyTemperatureRecord> {
    return convertReactRequestOptionsFromJS(BasalBodyTemperatureRecord::class, readableMap)
  }
}
