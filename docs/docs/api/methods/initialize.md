---
sidebar_position: 3
title: initialize
---

# `initialize`

Initializes the health connect client with specific providers. If `providerPackageNames` is not provided, the default Health Connect application package name will be considered `com.google.android.apps.healthdata`.

# Method

```ts
initialize(providerPackageNames: string[] = [DEFAULT_PROVIDER_PACKAGE_NAME]): Promise<boolean>;
```

# Example

```ts
import { initialize } from 'react-native-health-connect';

const onInitialize = async () => {
  const isInitialized = await initialize();
  console.log({ isInitialized });
};
```
