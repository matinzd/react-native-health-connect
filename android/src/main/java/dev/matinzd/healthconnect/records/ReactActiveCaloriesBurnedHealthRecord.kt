package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import java.time.Instant

class ReactActiveCaloriesBurnedHealthRecord : ReactHealthRecordImpl {
  private fun parseEnergy(energy: HashMap<*, *>): Energy {
    val value = energy["value"] as Double
    return when (energy["unit"]) {
      "kilojoules" -> Energy.kilocalories(value)
      "kilocalories" -> Energy.kilojoules(value)
      "joules" -> Energy.joules(value)
      "calories" -> Energy.calories(value)
      else -> Energy.calories(value)
    }
  }

  override fun parseWriteRecord(readableArray: ReadableArray): List<ActiveCaloriesBurnedRecord> {
    return readableArray.toArrayList().mapNotNull {
      it as HashMap<*, *>
      ActiveCaloriesBurnedRecord(
        startTime = Instant.parse(it["startTime"].toString()),
        endTime = Instant.parse(it["endTime"].toString()),
        energy = parseEnergy(it["energy"] as HashMap<*, *>),
        endZoneOffset = null,
        startZoneOffset = null
      )
    }.toList()
  }

  override fun parseReadRequest(readableMap: ReadableMap): ReadRecordsRequest<ActiveCaloriesBurnedRecord> {
    return ReadRecordsRequest(
      ActiveCaloriesBurnedRecord::class,
      timeRangeFilter = TimeRangeFilter.between(
        Instant.parse(readableMap.getString("startTime")),
        Instant.parse(readableMap.getString("endTime")),
      ),
      dataOriginFilter = readableMap.getArray("dataOriginFilter")?.toArrayList()
        ?.mapNotNull { DataOrigin(it.toString()) }?.toSet() ?: emptySet(),
      ascendingOrder = if(readableMap.hasKey("ascendingOrder")) readableMap.getBoolean("ascendingOrder") else true,
      pageSize = if(readableMap.hasKey("pageSize"))  readableMap.getInt("pageSize") else 1000,
      pageToken = if(readableMap.hasKey("pageToken"))  readableMap.getString("pageToken") else null,
    )
  }

  override fun parseReadResponse(response: ReadRecordsResponse<out Record>?): WritableNativeArray {
    @Suppress("UNCHECKED_CAST")
    val castedResponse = response as ReadRecordsResponse<ActiveCaloriesBurnedRecord>
    val reactArray = WritableNativeArray()
    for (record in castedResponse.records) {
      val reactMap = WritableNativeMap()
      reactMap.putString("startTime", record.startTime.toString())
      reactMap.putString("endTime", record.startTime.toString())
      reactMap.putDouble("energy", record.energy.inCalories)
      reactMap.putMap("metadata", ReactHealthRecord.extractMetadata(record.metadata))
      reactArray.pushMap(reactMap)
    }
    return reactArray
  }
}
