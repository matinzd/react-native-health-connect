package dev.matinzd.healthconnect

import com.facebook.react.bridge.*

class HealthConnectModule internal constructor(context: ReactApplicationContext) :
  HealthConnectSpec(context) {

  private val manager = HealthConnectManager(context)

  override fun getName(): String {
    return NAME
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
    readableArray: ReadableArray,
    providerPackageName: String,
    promise: Promise
  ) {
    return manager.requestPermission(readableArray, providerPackageName, promise)
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
