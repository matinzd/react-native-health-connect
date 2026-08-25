---
title: Read Health Data History Permission
---

# Read Health Data History Permission

Health Connect provides a special permission that allows your app to read health data recorded
before it was granted access, rather than only data written from the grant date forward. This is
useful for apps that want to show historical trends the first time a user connects their data.

## Setup

### 1. Add the permission to your AndroidManifest.xml

First, you need to declare the health data history permission in your app's `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.health.READ_HEALTH_DATA_HISTORY"/>
```

### 2. Request the permission in your app

Then, include the health data history permission in your permission request:

```ts
import { requestPermission } from 'react-native-health-connect';

const requestPermissions = () => {
  requestPermission([
    {
      accessType: 'read',
      recordType: 'ReadHealthDataHistory',
    },
    // Other permissions...
  ]).then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```

## Android Implementation

Under the hood, this permission maps to `HealthPermission.PERMISSION_READ_HEALTH_DATA_HISTORY` in the Android Health Connect API. Without it, reads are limited to records created after your app was first granted access.

## Checking for Health Data History Access

You can check if your app has been granted health data history permission using the `getGrantedPermissions` method:

```ts
import { getGrantedPermissions } from 'react-native-health-connect';

const checkHistoryAccess = async () => {
  const permissions = await getGrantedPermissions();
  const hasHistoryAccess = permissions.some(
    (permission) =>
      permission.accessType === 'read' &&
      permission.recordType === 'ReadHealthDataHistory'
  );

  console.log('Has health data history access:', hasHistoryAccess);
};
```

## Important Notes

- This permission is only available on Android devices with Health Connect support
- Make sure to explain to users why your app needs access to their historical health data
