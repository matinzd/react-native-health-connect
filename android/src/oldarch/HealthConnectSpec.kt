package dev.matinzd.healthconnect

import com.facebook.react.bridge.*

abstract class HealthConnectSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  @ReactMethod
  abstract fun isAvailable(promise: Promise)

  @ReactMethod
  abstract fun initialize(promise: Promise);

  @ReactMethod
  abstract fun requestPermission(permissions: ReadableArray, promise: Promise);

  @ReactMethod
  abstract fun revokeAllPermissions(promise: Promise);

  @ReactMethod
  abstract fun insertRecords(records: ReadableArray, promise: Promise);

  @ReactMethod
  abstract fun readRecords(recordType: String, options: ReadableMap, promise: Promise);
}
