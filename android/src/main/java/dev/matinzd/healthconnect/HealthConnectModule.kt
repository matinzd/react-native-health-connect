package dev.matinzd.healthconnect

import com.facebook.react.bridge.*

class HealthConnectModule internal constructor(context: ReactApplicationContext) :
  HealthConnectSpec(context) {

  private val manager = HealthConnectManager(context)

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun getSdkStatus(providerPackageName: String, promise: Promise) {
    return manager.getSdkStatus(providerPackageName, promise)
  }

  @ReactMethod
  override fun openHealthConnectSettings() {
    manager.openHealthConnectSettings()
  }

  @ReactMethod
  override fun initialize(providerPackageName: String, promise: Promise) {
    return manager.initialize(providerPackageName, promise)
  }

  @ReactMethod
  override fun requestPermission(
    permissions: ReadableArray,
    providerPackageName: String,
    promise: Promise
  ) {
    return manager.requestPermission(permissions, providerPackageName, promise)
  }

  @ReactMethod
  override fun getGrantedPermissions(promise: Promise) {
    return manager.getGrantedPermissions(promise)
  }

  @ReactMethod
  override fun revokeAllPermissions(promise: Promise) {
    return manager.revokeAllPermissions(promise)
  }

  @ReactMethod
  override fun insertRecords(records: ReadableArray, promise: Promise) {
    return manager.insertRecords(records, promise)
  }

  @ReactMethod
  override fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    return manager.readRecords(recordType, options, promise)
  }

  @ReactMethod
  override fun readRecord(recordType: String, recordId: String, promise: Promise) {
    return manager.readRecord(recordType, recordId, promise)
  }

  @ReactMethod
  override fun aggregateRecord(record: ReadableMap, promise: Promise) {
    return manager.aggregateRecord(record, promise)
  }

  @ReactMethod
  override fun deleteRecordsByUuids(
    recordType: String,
    recordIdsList: ReadableArray,
    clientRecordIdsList: ReadableArray,
    promise: Promise
  ) {
    return manager.deleteRecordsByUuids(recordType, recordIdsList, clientRecordIdsList, promise)
  }

  @ReactMethod
  override fun deleteRecordsByTimeRange(
    recordType: String,
    timeRangeFilter: ReadableMap,
    promise: Promise
  ) {
    return manager.deleteRecordsByTimeRange(recordType, timeRangeFilter, promise)
  }

  companion object {
    const val NAME = "HealthConnect"
  }
}
