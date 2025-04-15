---
title: requestPermission
---

# `requestPermission`

Request permission for specified record types and access types.

# Method

```ts
requestPermission(permissions: Permission[]): Promise<Permission[]>
```

# Example

```ts
import { requestPermission } from 'react-native-health-connect';

const requestPermissions = () => {
  requestPermission([
    {
      accessType: 'read',
      recordType: 'ActiveCaloriesBurned',
    },
    {
      accessType: 'write',
      recordType: 'ActiveCaloriesBurned',
    },
  ]).then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```

## Special Permissions

### Exercise Route Permission

If your app needs to write exercise routes, you can include it as a special permission:

```ts
import { requestPermission } from 'react-native-health-connect';

const requestPermissions = () => {
  requestPermission([
    {
      accessType: 'read',
      recordType: 'ExerciseSession',
    },
    {
      accessType: 'write',
      recordType: 'ExerciseSession',
    },
    {
      accessType: 'write',
      recordType: 'ExerciseRoute'
    }
  ]).then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```

### Background Access Permission

If your app needs to read health data in the background, you can request the background access permission:

```ts
import { requestPermission } from 'react-native-health-connect';

const requestBackgroundAccess = () => {
  requestPermission([
    {
      accessType: 'read',
      recordType: 'BackgroundAccessPermission',
    },
    // Other permissions you need...
    {
      accessType: 'read',
      recordType: 'Steps',
    },
    {
      accessType: 'read',
      recordType: 'HeartRate',
    }
  ]).then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```