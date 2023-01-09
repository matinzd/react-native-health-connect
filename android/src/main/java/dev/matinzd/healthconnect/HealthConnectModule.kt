package dev.matinzd.healthconnect

import com.facebook.react.bridge.*

class HealthConnectModule internal constructor(context: ReactApplicationContext) :
  HealthConnectSpec(context) {

  private val manager = HealthConnectManager(context)

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun isAvailable(promise: Promise) {
    return promise.resolve(manager.isAvailable())
  }

  @ReactMethod
  override fun initialize(promise: Promise) {
    return manager.initialize(promise)
  }

  @ReactMethod
  override fun requestPermission(readableArray: ReadableArray, promise: Promise){
    return manager.requestPermission(readableArray, promise)
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

  companion object {
    const val NAME = "HealthConnect"
  }
}
