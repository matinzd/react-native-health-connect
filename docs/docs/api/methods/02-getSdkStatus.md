---
title: getSdkStatus
---

# `getSdkStatus`

Determines whether an implementation of HealthConnectClient is available on the device at the moment. If none is available, apps may choose to redirect to package installers to find suitable providers. If `providerPackageName` is not provided, the default Health Connect application package name will be considered `com.google.android.apps.healthdata`.

# Method

```ts
getSdkStatus(providerPackageName: string): Promise<number>;
```

# Example

```ts
import {
  getSdkStatus,
  SdkAvailabilityStatus,
} from 'react-native-health-connect';

const checkAvailability = async () => {
  const status = await getSdkStatus();
  if (status === SdkAvailabilityStatus.SDK_AVAILABLE) {
    console.log('SDK is available');
  }

  if (status === SdkAvailabilityStatus.SDK_UNAVAILABLE) {
    console.log('SDK is not available');
  }

  if (
    status === SdkAvailabilityStatus.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
  ) {
    console.log('SDK is not available, provider update required');
  }
};
```
