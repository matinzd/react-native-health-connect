package dev.matinzd.healthconnect

open class ExceptionWithCode(code: String, message: String): Exception(code, Exception(message))

class ClientNotInitialized: ExceptionWithCode("NOT_INITIALIZED","Health Connect client is not initialized")

class ClientNotAvailable: ExceptionWithCode("NOT_AVAILABLE","Client is not available on this device")

class InvalidRecordType: ExceptionWithCode("INVALID_RECORD_TYPE","Record type is not valid")
