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

If your app needs to write exercise routes, can include it as a special permission:

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