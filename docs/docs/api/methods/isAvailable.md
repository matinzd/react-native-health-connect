---
sidebar_position: 2
title: isAvailable
---

# `isAvailable`

Determines whether an implementation of HealthConnectClient is available on the device at the moment. If none is available, apps may choose to redirect to package installers to find suitable providers. If `providerPackageNames` is not provided, the default Health Connect application package name will be considered `com.google.android.apps.healthdata`.

# Method

```ts
isAvailable(providerPackageNames: string[] = [DEFAULT_PROVIDER_PACKAGE_NAME]): Promise<boolean>;
```

# Example

```ts
import { isAvailable } from 'react-native-health-connect';

const checkIfAvailable = async () => {
  const available = await isAvailable();
  console.log({ available });
};
```
