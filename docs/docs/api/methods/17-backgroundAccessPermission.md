---
title: Background Access Permission
---

# Background Access Permission

Health Connect provides a special permission that allows your app to read health data in the background. This is useful for apps that need to monitor health data continuously, such as fitness tracking apps.

## Setup

### 1. Add the permission to your AndroidManifest.xml

First, you need to declare the background access permission in your app's `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.health.READ_HEALTH_DATA_IN_BACKGROUND"/>
```

### 2. Request the permission in your app

Then, include the background access permission in your permission request:

```ts
import { requestPermission } from 'react-native-health-connect';

const requestPermissions = () => {
  requestPermission([
    {
      accessType: 'read',
      recordType: 'BackgroundAccessPermission',
    },
    // Other permissions...
  ]).then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```

## Android Implementation

Under the hood, this permission maps to `HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND` in the Android Health Connect API. This permission allows your app to read health data even when it's not in the foreground.

## Checking for Background Access

You can check if your app has been granted background access permission using the `getGrantedPermissions` method:

```ts
import { getGrantedPermissions } from 'react-native-health-connect';

const checkBackgroundAccess = async () => {
  const permissions = await getGrantedPermissions();
  const hasBackgroundAccess = permissions.some(
    (permission) =>
      permission.accessType === 'read' &&
      permission.recordType === 'BackgroundAccessPermission'
  );

  console.log('Has background access:', hasBackgroundAccess);
};
```

## Important Notes

- Background access is a powerful permission that should be used responsibly
- Make sure to explain to users why your app needs background access to their health data
- This permission is only available on Android devices with Health Connect support
