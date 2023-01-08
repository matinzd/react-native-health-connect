package dev.matinzd.healthconnect.records

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.InsertRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableNativeArray
import dev.matinzd.healthconnect.InvalidRecordType

class ReactRecordParser {
  companion object {
    private val recordParserClassMap: Map<String, Class<out ReactRecordImpl>> = mapOf(
      "activeCaloriesBurned" to ReactActiveCaloriesBurnedRecord::class.java
    )

    fun parseRecords(reactRecords: ReadableArray): List<Record> {
      val recordType = reactRecords.getMap(0).getString("recordType")

      val recordClass = recordParserClassMap[recordType]?.newInstance()
        ?: throw InvalidRecordType()

      return recordClass.parseReactArray(reactRecords)
    }

    fun parseResponse(response: InsertRecordsResponse?): WritableNativeArray {
      val ids = WritableNativeArray()
      response?.recordIdsList?.forEach { ids.pushString(it) }
      return ids
    }
  }
}
