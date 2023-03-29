package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactTotalCaloriesBurnedRecord : ReactHealthRecordImpl<TotalCaloriesBurnedRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<TotalCaloriesBurnedRecord> {
    return records.toMapList().map { map ->
      TotalCaloriesBurnedRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        energy = getEnergyFromJsMap(map.getMap("energy"))
      )
    }
  }

  override fun parseRecord(record: TotalCaloriesBurnedRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putMap("energy", energyToJsMap(record.energy))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        TotalCaloriesBurnedRecord.ENERGY_TOTAL
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      val map = WritableNativeMap().apply {
        putDouble(
          "inCalories",
          record[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inCalories ?: 0.0
        )
        putDouble(
          "inKilojoules",
          record[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilojoules ?: 0.0
        )
        putDouble(
          "inKilocalories",
          record[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
        )
        putDouble(
          "inJoules",
          record[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inJoules ?: 0.0
        )
      }
      putMap("ENERGY_TOTAL", map)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
