package dev.matinzd.healthconnect

import android.os.RemoteException
import com.facebook.react.bridge.Promise
import okio.IOException

class ClientNotInitialized : Exception("Health Connect client is not initialized")
class ClientNotAvailable : Exception( "Client is not available on this device")
class InvalidRecordType : Exception("Record type is not valid")

fun Promise.rejectWithException(exception: Exception) {
  val code = when (exception) {
    is SecurityException -> "PERMISSION_ERROR"
    is UnsupportedOperationException -> "SDK_VERSION_ERROR"
    is IOException -> "IO_EXCEPTION"
    is IllegalStateException -> "VALIDATION_ERROR"
    is IllegalArgumentException -> "ARGUMENT_VALIDATION_ERROR"
    is RemoteException -> "UNDERLYING_ERROR"
    is ClientNotAvailable -> "CLIENT_UNAVAILABLE"
    is InvalidRecordType -> "INVALID_RECORD_TYPE"
    is ClientNotInitialized -> "CLIENT_NOT_INITIALIZED"
    else -> "UNKNOWN_ERROR"
  }

  this.reject(code, exception.message)
}
