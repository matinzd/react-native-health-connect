package dev.matinzd.healthconnect

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.facebook.react.bridge.*
import java.util.concurrent.TimeUnit


class HealthConnectModule internal constructor(context: ReactApplicationContext) :
  HealthConnectSpec(context) {

  private val manager = HealthConnectManager(context)
  private val WORK_NAME = "HealthConnectTask"

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun startPeriodicBackgroundWorker(promise: Promise) {
    val workRequest =
      PeriodicWorkRequest.Builder(HealthConnectWorker::class.java, 15, TimeUnit.MINUTES).build()
    WorkManager.getInstance(reactApplicationContext)
      .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
  }

  @ReactMethod
  override fun cancelPeriodicBackgroundWorker(promise: Promise) {
    WorkManager.getInstance(reactApplicationContext).cancelUniqueWork(WORK_NAME)
  }

  @ReactMethod
  override fun isAvailable(providerPackageNames: ReadableArray, promise: Promise) {
    return manager.isAvailable(providerPackageNames, promise)
  }

  @ReactMethod
  override fun initialize(providerPackageNames: ReadableArray, promise: Promise) {
    return manager.initialize(providerPackageNames, promise)
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
  override fun aggregateRecord(record: ReadableMap, promise: Promise) {
    return manager.aggregateRecord(record, promise)
  }

  override fun deleteRecordsByUuids(
    recordType: String,
    recordIdsList: ReadableArray,
    clientRecordIdsList: ReadableArray,
    promise: Promise
  ) {
    return manager.deleteRecordsByUuids(recordType, recordIdsList, clientRecordIdsList, promise)
  }

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
