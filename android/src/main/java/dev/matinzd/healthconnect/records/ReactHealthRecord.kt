package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.InvalidRecordType
import dev.matinzd.healthconnect.utils.convertReactRequestOptionsFromJS
import dev.matinzd.healthconnect.utils.healthConnectClassToReactClassMap
import dev.matinzd.healthconnect.utils.reactClassToReactTypeMap
import dev.matinzd.healthconnect.utils.reactRecordTypeToClassMap
import dev.matinzd.healthconnect.utils.reactRecordTypeToReactClassMap
import kotlin.reflect.KClass

class ReactHealthRecord {
  companion object {
    private fun <T : Record> createReactHealthRecordInstance(recordType: String?): ReactHealthRecordImpl<T> {
      if (!reactRecordTypeToReactClassMap.containsKey(recordType)) {
        throw InvalidRecordType()
      }

      val reactClass = reactRecordTypeToReactClassMap[recordType]
      return reactClass?.newInstance() as ReactHealthRecordImpl<T>
    }

    private fun <T : Record> createReactHealthRecordInstance(recordClass: Class<out Record>): ReactHealthRecordImpl<T> {
      if (!healthConnectClassToReactClassMap.containsKey(recordClass)) {
        throw InvalidRecordType()
      }

      val reactClass = healthConnectClassToReactClassMap[recordClass]
      return reactClass?.newInstance() as ReactHealthRecordImpl<T>
    }

    fun getRecordByType(recordType: String): KClass<out Record> {
      if (!reactRecordTypeToClassMap.containsKey(recordType)) {
        throw InvalidRecordType()
      }

      return reactRecordTypeToClassMap[recordType]!!
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
      return convertReactRequestOptionsFromJS(getRecordByType(recordType), reactRequest)
    }

    fun getAggregateRequest(recordType: String, reactRequest: ReadableMap): AggregateRequest {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.getAggregateRequest(reactRequest)
    }

    fun getAggregateGroupByPeriodRequest(recordType: String, reactRequest: ReadableMap): AggregateGroupByPeriodRequest {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.getAggregateGroupByPeriodRequest(reactRequest)
    }

    fun getAggregateGroupByDurationRequest(recordType: String, reactRequest: ReadableMap): AggregateGroupByDurationRequest {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.getAggregateGroupByDurationRequest(reactRequest)
    }

    fun parseAggregationResult(recordType: String, result: AggregationResult): WritableNativeMap {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseAggregationResult(result)
    }

    fun parseAggregationResultGroupedByDuration(recordType: String, result: List<AggregationResultGroupedByDuration>): WritableNativeArray {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseAggregationResultGroupedByDuration(result)
    }

    fun parseAggregationResultGroupedByPeriod(recordType: String, result: List<AggregationResultGroupedByPeriod>): WritableNativeArray {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)

      return recordClass.parseAggregationResultGroupedByPeriod(result)
    }

    fun parseRecords(
      recordType: String,
      response: ReadRecordsResponse<out Record>
    ): WritableNativeMap {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)
      return WritableNativeMap().apply {
        putString("pageToken", response.pageToken)
        putArray("records", WritableNativeArray().apply {
          for (record in response.records) {
            pushMap(recordClass.parseRecord(record))
          }
        })
      }
    }

    fun parseRecord(
      recordType: String,
      response: ReadRecordResponse<out Record>
    ): WritableNativeMap {
      val recordClass = createReactHealthRecordInstance<Record>(recordType)
      return recordClass.parseRecord(response.record)
    }

    fun parseRecord(
      record: Record
    ): WritableNativeMap {
      val reactRecordClass = createReactHealthRecordInstance<Record>(record.javaClass)
      val reactRecord = reactRecordClass.parseRecord(record)
      reactRecord.putString("recordType", reactClassToReactTypeMap[reactRecordClass.javaClass])
      return reactRecord
    }
  }
}
