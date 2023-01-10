package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.units.Energy
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.HealthConnectUtils
import java.time.Instant

class ReactActiveCaloriesBurnedHealthRecord : ReactHealthRecordImpl {
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
    val map = WritableNativeMap()
    map.putDouble("inCalories", energy.inCalories)
    map.putDouble("inJoules", energy.inJoules)
    map.putDouble("inKilocalories", energy.inKilocalories)
    map.putDouble("inKilojoules", energy.inKilojoules)
    return map
  }

  override fun parseWriteRecord(readableArray: ReadableArray): List<ActiveCaloriesBurnedRecord> {
    return readableArray.toArrayList().mapNotNull {
      it as HashMap<*, *>
      ActiveCaloriesBurnedRecord(
        startTime = Instant.parse(it["startTime"].toString()),
        endTime = Instant.parse(it["endTime"].toString()),
        energy =  getEnergyFromJsMap(it["energy"] as HashMap<*, *>),
        endZoneOffset = null,
        startZoneOffset = null
      )
    }.toList()
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<ActiveCaloriesBurnedRecord> {
    return HealthConnectUtils.convertReactRequestOptionsFromJS(ActiveCaloriesBurnedRecord::class, readableMap)
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out Record>?): WritableNativeArray {
    @Suppress("UNCHECKED_CAST")
    val castedResponse = response as ReadRecordsResponse<ActiveCaloriesBurnedRecord>
    val reactArray = WritableNativeArray()
    for (record in castedResponse.records) {
      val reactMap = WritableNativeMap()
      reactMap.putString("startTime", record.startTime.toString())
      reactMap.putString("endTime", record.startTime.toString())
      reactMap.putMap("energy", energyToJsMap(record.energy))
      reactMap.putMap("metadata", ReactHealthRecord.extractMetadata(record.metadata))
      reactArray.pushMap(reactMap)
    }
    return reactArray
  }
}
