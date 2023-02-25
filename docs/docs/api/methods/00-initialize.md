---
title: initialize
---

# `initialize`

Initializes the health connect client with specific providers. If `providerPackageName` is not provided, the default Health Connect application package name will be considered `com.google.android.apps.healthdata`.

# Method

```ts
initialize(providerPackageName: string): Promise<boolean>;
```

# Example

```ts
import { initialize } from 'react-native-health-connect';

const initializeHealthConnect = async () => {
  const isInitialized = await initialize();
  console.log({ isInitialized });
};
```
