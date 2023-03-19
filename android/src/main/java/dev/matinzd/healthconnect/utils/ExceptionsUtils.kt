package dev.matinzd.healthconnect.utils

import android.os.RemoteException
import com.facebook.react.bridge.Promise
import okio.IOException

class ClientNotInitialized : Exception("Health Connect client is not initialized")
class InvalidRecordType : Exception("Record type is not valid")
class InvalidTemperature : Exception("Temperature is not valid")
class InvalidEnergy : Exception("Energy is not valid")
class InvalidVelocity : Exception("Velocity is not valid")
class InvalidPower : Exception("Power is not valid")
class InvalidBloodGlucoseLevel : Exception("Blood glucose level is not valid")
class InvalidBloodPressure : Exception("Blood pressure is not valid")
class InvalidMass : Exception("Mass is not valid")
class InvalidLength : Exception("Length is not valid")
class AggregationNotSupported : Exception("Aggregation is not supported for this record")

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
    is InvalidTemperature -> "INVALID_TEMPERATURE"
    is InvalidEnergy -> "INVALID_ENERGY"
    is InvalidPower -> "INVALID_POWER"
    is InvalidBloodGlucoseLevel -> "INVALID_BLOOD_GLUCOSE_LEVEL"
    is InvalidBloodPressure -> "INVALID_BLOOD_PRESSURE"
    is InvalidMass -> "INVALID_MASS"
    is AggregationNotSupported -> "AGGREGATION_NOT_SUPPORTED"
    else -> "UNKNOWN_ERROR"
  }

  this.reject(code, exception.message)
}
