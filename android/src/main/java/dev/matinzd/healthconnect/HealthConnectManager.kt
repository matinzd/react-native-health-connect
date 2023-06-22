package dev.matinzd.healthconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.health.connect.client.HealthConnectClient
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import dev.matinzd.healthconnect.permissions.HCPermissionManager
import dev.matinzd.healthconnect.records.ReactHealthRecord
import dev.matinzd.healthconnect.utils.ClientNotInitialized
import dev.matinzd.healthconnect.utils.getTimeRangeFilter
import dev.matinzd.healthconnect.utils.reactRecordTypeToClassMap
import dev.matinzd.healthconnect.utils.rejectWithException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthConnectManager(private val applicationContext: ReactApplicationContext) :
  ActivityEventListener {
  private lateinit var healthConnectClient: HealthConnectClient
  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  private var pendingPromise: Promise? = null
  private var latestPermissions: Set<String>? = null

  private val isInitialized get() = this::healthConnectClient.isInitialized

  private inline fun throwUnlessClientIsAvailable(promise: Promise, block: () -> Unit) {
    if (!isInitialized) {
      return promise.rejectWithException(ClientNotInitialized())
    }
    block()
  }

  override fun onActivityResult(
    activity: Activity?, requestCode: Int, resultCode: Int, intent: Intent?
  ) {
    if (requestCode == REQUEST_CODE) {
      HCPermissionManager.parseOnActivityResult(resultCode, intent, pendingPromise)
    }
  }

  override fun onNewIntent(intent: Intent?) {}

  fun openHealthConnectSettings() {
    val intent = Intent(HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS)
    applicationContext.currentActivity?.startActivity(intent)
  }

  fun getSdkStatus(providerPackageName: String, promise: Promise) {
    val status = HealthConnectClient.getSdkStatus(applicationContext, providerPackageName)
    return promise.resolve(status)
  }

  fun initialize(providerPackageName: String, promise: Promise) {
    try {
      healthConnectClient = HealthConnectClient.getOrCreate(applicationContext, providerPackageName)
      promise.resolve(true)
    } catch (e: Exception) {
      promise.rejectWithException(e)
    }
  }

  fun requestPermission(
    reactPermissions: ReadableArray, providerPackageName: String, promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      this.pendingPromise = promise
      this.latestPermissions = HCPermissionManager.parsePermissions(reactPermissions)

      val bundle = Bundle().apply {
        putString("providerPackageName", providerPackageName)
      }

      val intent = HCPermissionManager(providerPackageName).healthPermissionContract.createIntent(
        applicationContext, latestPermissions!!
      )

      applicationContext.currentActivity?.startActivityForResult(
        intent,
        HealthConnectManager.REQUEST_CODE,
        bundle
      )
    }
  }

  fun revokeAllPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        healthConnectClient.permissionController.revokeAllPermissions()
      }
    }
  }

  fun getGrantedPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        promise.resolve(HCPermissionManager.getGrantedPermissions(healthConnectClient.permissionController))
      }
    }
  }

  fun insertRecords(reactRecords: ReadableArray, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val records = ReactHealthRecord.parseWriteRecords(reactRecords)
          val response = healthConnectClient.insertRecords(records)
          promise.resolve(ReactHealthRecord.parseWriteResponse(response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val request = ReactHealthRecord.parseReadRequest(recordType, options)
          val response = healthConnectClient.readRecords(request)
          promise.resolve(ReactHealthRecord.parseRecords(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun readRecord(recordType: String, recordId: String, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val record = ReactHealthRecord.getRecordByType(recordType)
          val response = healthConnectClient.readRecord(record, recordId)
          promise.resolve(ReactHealthRecord.parseRecord(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun aggregateRecord(record: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val recordType = record.getString("recordType") ?: ""
          val response = healthConnectClient.aggregate(
            ReactHealthRecord.getAggregateRequest(
              recordType, record
            )
          )
          promise.resolve(ReactHealthRecord.parseAggregationResult(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun deleteRecordsByUuids(
    recordType: String,
    recordIdsList: ReadableArray,
    clientRecordIdsList: ReadableArray,
    promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        val record = reactRecordTypeToClassMap[recordType]
        if (record != null) {
          healthConnectClient.deleteRecords(
            recordType = record,
            recordIdsList = recordIdsList.toArrayList().mapNotNull { it.toString() }.toList(),
            clientRecordIdsList = if (clientRecordIdsList.size() > 0) clientRecordIdsList.toArrayList()
              .mapNotNull { it.toString() }.toList() else emptyList()
          )
        }
      }
    }
  }

  fun deleteRecordsByTimeRange(
    recordType: String, timeRangeFilter: ReadableMap, promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        val record = reactRecordTypeToClassMap[recordType]
        if (record != null) {
          healthConnectClient.deleteRecords(
            recordType = record, timeRangeFilter = timeRangeFilter.getTimeRangeFilter()
          )
        }
      }
    }
  }

  companion object {
    const val REQUEST_CODE = 4235
  }

  init {
    applicationContext.addActivityEventListener(this)
  }
}

