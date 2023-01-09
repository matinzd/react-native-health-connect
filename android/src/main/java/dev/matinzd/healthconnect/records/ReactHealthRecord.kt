package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.InvalidRecordType
import androidx.health.connect.client.records.metadata.Metadata
import com.facebook.react.bridge.WritableNativeMap

class ReactHealthRecord {
  companion object {
    private val recordParserClassMap: Map<String, Class<out ReactHealthRecordImpl>> = mapOf(
      "activeCaloriesBurned" to ReactActiveCaloriesBurnedHealthRecord::class.java,
      "basalBodyTemperature" to ReactBasalBodyTemperatureHealthRecord::class.java,
      "basalMetabolicRate" to ReactBasalMetabolicRateHealthRecord::class.java,
      "bloodGlucose" to ReactBloodGlucoseHealthRecord::class.java,
    )

    fun extractMetadata(meta: Metadata): WritableNativeMap {
      val metadata = WritableNativeMap()
      metadata.putString("id", meta.id)
      metadata.putString("clientRecordId", meta.clientRecordId)
      metadata.putDouble("clientRecordVersion", meta.clientRecordVersion.toDouble())
      metadata.putString("dataOrigin", meta.dataOrigin.packageName)
      metadata.putString("lastModifiedTime", meta.lastModifiedTime.toString())
      metadata.putInt("device", meta.device?.type ?: 0)
      return metadata
    }

    fun parseWriteRecords(reactRecords: ReadableArray): List<Record> {
      val recordType = reactRecords.getMap(0).getString("recordType")

      val recordClass = recordParserClassMap[recordType]?.newInstance()
        ?: throw InvalidRecordType()

      return recordClass.parseWriteRecord(reactRecords)
    }

    fun parseWriteResponse(response: InsertRecordsResponse?): WritableNativeArray {
      val ids = WritableNativeArray()
      response?.recordIdsList?.forEach { ids.pushString(it) }
      return ids
    }

    fun parseReadRequest(recordType: String, reactRequest: ReadableMap): ReadRecordsRequest<out Record> {
      val recordClass = recordParserClassMap[recordType]?.newInstance()
        ?: throw InvalidRecordType()

      return recordClass.parseReadRequest(reactRequest)
    }

    fun parseReadResponse(recordType: String, response: ReadRecordsResponse<out Record>?): WritableNativeArray {
      val recordClass = recordParserClassMap[recordType]?.newInstance()
        ?: throw InvalidRecordType()

      return recordClass.parseReadResponse(response)
    }
  }
}
