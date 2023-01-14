package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.units.Energy
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS
import java.time.Instant

class ReactActiveCaloriesBurnedRecord : ReactHealthRecordImpl<ActiveCaloriesBurnedRecord> {
  private fun getEnergyFromJsMap(energy: HashMap<*, *>): Energy {
    val value = energy["value"] as Double
    return when (energy["unit"]) {
      "kilojoules" -> Energy.kilocalories(value)
      "kilocalories" -> Energy.kilojoules(value)
      "joules" -> Energy.joules(value)
      "calories" -> Energy.calories(value)
      else -> Energy.calories(value)
    }
  }

  private fun energyToJsMap(energy: Energy): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inCalories", energy.inCalories)
      putDouble("inJoules", energy.inJoules)
      putDouble("inKilocalories", energy.inKilocalories)
      putDouble("inKilojoules", energy.inKilojoules)
    }
  }

  override fun parseWriteRecord(readableArray: ReadableArray): List<ActiveCaloriesBurnedRecord> {
    return readableArray.toArrayList().mapNotNull {
      it as HashMap<*, *>
      ActiveCaloriesBurnedRecord(
        startTime = Instant.parse(it["startTime"].toString()),
        endTime = Instant.parse(it["endTime"].toString()),
        energy = getEnergyFromJsMap(it["energy"] as HashMap<*, *>),
        endZoneOffset = null,
        startZoneOffset = null
      )
    }.toList()
  }


  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<ActiveCaloriesBurnedRecord> {
    return convertReactRequestOptionsFromJS(ActiveCaloriesBurnedRecord::class, readableMap)
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out ActiveCaloriesBurnedRecord>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (record in response.records) {
        val reactMap = WritableNativeMap().apply {
          putString("startTime", record.startTime.toString())
          putString("endTime", record.startTime.toString())
          putMap("energy", energyToJsMap(record.energy))
          putMap("metadata", convertMetadataToJSMap(record.metadata))
        }
        pushMap(reactMap)
      }
    }
  }
}
