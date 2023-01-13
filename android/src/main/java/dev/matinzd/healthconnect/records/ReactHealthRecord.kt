package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.utils.InvalidRecordType

class ReactHealthRecord {
  companion object {
    private val reactRecordTypeToReactClassMap: Map<String, Class<out ReactHealthRecordImpl<*>>> =
      mapOf(
        "activeCaloriesBurned" to ReactActiveCaloriesBurnedRecord::class.java,
        "basalBodyTemperature" to ReactBasalBodyTemperatureRecord::class.java,
        "basalMetabolicRate" to ReactBasalMetabolicRateRecord::class.java,
        "bloodGlucose" to ReactBloodGlucoseRecord::class.java,
      )

    private fun <T : Record> createReactHealthRecordInstance(recordType: String?): ReactHealthRecordImpl<T> {
      if (!reactRecordTypeToReactClassMap.containsKey(recordType)) {
        throw InvalidRecordType()
      }

      val reactClass = reactRecordTypeToReactClassMap[recordType]
      return reactClass?.newInstance() as ReactHealthRecordImpl<T>
    }

    fun parseWriteRecords(reactRecords: ReadableArray): List<Record> {
      val recordType = reactRecords.getMap(0).getString("recordType")

      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseWriteRecord(reactRecords)
    }

    fun parseWriteResponse(response: InsertRecordsResponse?): WritableNativeArray {
      val ids = WritableNativeArray()
      response?.recordIdsList?.forEach { ids.pushString(it) }
      return ids
    }

    fun parseReadRequest(recordType: String, reactRequest: ReadableMap): ReadRecordsRequest<*> {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseReadRequest(reactRequest)
    }

    fun parseReadResponse(
      recordType: String,
      response: ReadRecordsResponse<out Record>
    ): WritableNativeArray {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)
      return recordClass.parseReadResponse(response)
    }
  }
}
