package dev.matinzd.healthconnect

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray

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
  override fun revokeAllPermissions() {
    return manager.revokeAllPermissions()
  }

  @ReactMethod
  override fun insertRecords(records: ReadableArray, promise: Promise) {
    return manager.insertRecords(records, promise)
  }

  companion object {
    const val NAME = "HealthConnect"
  }
}
