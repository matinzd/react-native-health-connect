package dev.matinzd.healthconnect.records

import android.util.Log
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.units.Energy
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableNativeArray
import java.time.Instant

class ReactActiveCaloriesBurnedRecord : ReactRecordImpl {
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

  override fun parseReactArray(readableArray: ReadableArray): List<ActiveCaloriesBurnedRecord> {
    return readableArray.toArrayList().mapNotNull { it as HashMap<*, *>
      ActiveCaloriesBurnedRecord(
        startTime = Instant.parse(it["startTime"].toString()),
        endTime = Instant.parse(it["endTime"].toString()),
        energy = parseEnergy(it["energy"] as HashMap<*, *>),
        endZoneOffset = null,
        startZoneOffset = null
      )
    }.toList()
  }
}
