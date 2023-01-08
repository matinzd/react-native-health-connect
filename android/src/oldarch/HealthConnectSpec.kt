package dev.matinzd.healthconnect

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise

abstract class HealthConnectSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  abstract fun isAvailable(promise: Promise)
  abstract fun initialize(promise: Promise)
  abstract fun requestPermission(promise: Promise)
  abstract fun revokeAllPermissions()
  abstract fun insertRecords(ReadableArray records, Promise promise)
}
