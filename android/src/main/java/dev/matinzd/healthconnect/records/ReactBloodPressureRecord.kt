package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.Pressure
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactBloodPressureRecord : ReactHealthRecordImpl<BloodPressureRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<BloodPressureRecord> {
    return records.toMapList().map {
      BloodPressureRecord(
        time = Instant.parse(it.getString("time")),
        systolic = getBloodPressureFromJsMap(it.getMap("systolic")),
        diastolic = getBloodPressureFromJsMap(it.getMap("diastolic")),
        bodyPosition = it.getSafeInt("bodyPosition", BloodPressureRecord.BODY_POSITION_UNKNOWN),
        measurementLocation = it.getSafeInt(
          "measurementLocation", BloodPressureRecord.MEASUREMENT_LOCATION_UNKNOWN
        ),
        zoneOffset = null
      )
    }
  }

  override fun parseRecord(record: BloodPressureRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putInt("measurementLocation", record.measurementLocation)
      putInt("bodyPosition", record.bodyPosition)
      putMap("systolic", bloodPressureToJsMap(record.systolic))
      putMap("diastolic", bloodPressureToJsMap(record.diastolic))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        BloodPressureRecord.SYSTOLIC_AVG,
        BloodPressureRecord.SYSTOLIC_MIN,
        BloodPressureRecord.SYSTOLIC_MAX,
        BloodPressureRecord.DIASTOLIC_AVG,
        BloodPressureRecord.DIASTOLIC_MIN,
        BloodPressureRecord.DIASTOLIC_MAX
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {

      putMap(
        "SYSTOLIC_AVG",
        getPressureMap(record[BloodPressureRecord.SYSTOLIC_AVG]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "SYSTOLIC_MIN",
        getPressureMap(record[BloodPressureRecord.SYSTOLIC_MIN]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "DIASTOLIC_AVG",
        getPressureMap(record[BloodPressureRecord.DIASTOLIC_AVG]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "DIASTOLIC_MIN",
        getPressureMap(record[BloodPressureRecord.DIASTOLIC_MIN]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "DIASTOLIC_MAX",
        getPressureMap(record[BloodPressureRecord.DIASTOLIC_MAX]?.inMillimetersOfMercury ?: 0.0)
      )
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  private fun bloodPressureToJsMap(pressure: Pressure): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inMillimetersOfMercury", pressure.inMillimetersOfMercury)
    }
  }

  private fun getBloodPressureFromJsMap(bloodPressureMap: ReadableMap?): Pressure {
    if (bloodPressureMap == null) {
      throw InvalidBloodPressure()
    }

    val value = bloodPressureMap.getDouble("value")
    return Pressure.millimetersOfMercury(value)
  }

  private fun getPressureMap(inMillimetersOfMercury: Double): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inMillimetersOfMercury", inMillimetersOfMercury)
    }
  }
}
