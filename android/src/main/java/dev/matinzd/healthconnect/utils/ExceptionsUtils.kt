package dev.matinzd.healthconnect.utils

import android.os.RemoteException
import com.facebook.react.bridge.Promise
import okio.IOException

class ClientNotInitialized : Exception("Health Connect client is not initialized")
class InvalidRecordType : Exception("Record type is not valid")

fun Promise.rejectWithException(exception: Exception) {
  val code = when (exception) {
    is SecurityException -> "PERMISSION_ERROR"
    is UnsupportedOperationException -> "SDK_VERSION_ERROR"
    is IOException -> "IO_EXCEPTION"
    is IllegalStateException -> "SERVICE_UNAVAILABLE"
    is IllegalArgumentException -> "ARGUMENT_VALIDATION_ERROR"
    is RemoteException -> "UNDERLYING_ERROR"
    is InvalidRecordType -> "INVALID_RECORD_TYPE"
    is ClientNotInitialized -> "CLIENT_NOT_INITIALIZED"
    else -> "UNKNOWN_ERROR"
  }

  this.reject(code, exception.message)
}
