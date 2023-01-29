---
sidebar_position: 3
title: Exceptions
---

## Exceptions

The SDK throws exceptions for operations when an issue is encountered and you should make sure that these are appropriately caught and handled. Each exception contains a code and a message that describes the error. In general, the following exceptions may occur:

- `PERMISSION_ERROR` - This exception is thrown when the SDK does not have the required permissions to perform the operation. You need to define nessesary permissions inside your application manifest as described [here](../permissions.md).
- `SDK_VERSION_ERROR` - This exception is thrown when the SDK version is incompatible with the service or the operation being performed.
- `IO_EXCEPTION` - This exception is thrown when there is an issue with input/output operations, such as file reading or writing.
- `SERVICE_UNAVAILABLE` - This exception is thrown when the service that the SDK is trying to connect to is unavailable on device or not supported.
- `ARGUMENT_VALIDATION_ERROR` - This exception is thrown when one or more of the arguments passed to a method is invalid.
- `UNDERLYING_ERROR` - This exception is thrown when there is an error with the underlying service or technology the SDK is using.
- `INVALID_RECORD_TYPE` - This exception is thrown when the type of record passed to a method is not valid.
- `CLIENT_NOT_INITIALIZED` - This exception is thrown when the SDK client has not been properly initialized.
- `INVALID_TEMPERATURE` - This exception is thrown when an invalid temperature value is passed to a method.
- `INVALID_ENERGY` - This exception is thrown when an invalid energy value is passed to a method.
- `INVALID_BLOOD_GLUCOSE_LEVEL` - This exception is thrown when an invalid blood glucose value is passed to a method.
- `INVALID_BLOOD_PRESSURE` - This exception is thrown when an invalid blood pressure value is passed to a method.
- `INVALID_MASS` - This exception is thrown when an invalid mass value is passed to a method.
- `AGGREGATION_NOT_SUPPORTED` - This exception is thrown for some records that does not support aggregation metrics.
- `UNKNOWN_ERROR` - This exception is thrown when the SDK encounters an error that is not covered by any other specific exception.
