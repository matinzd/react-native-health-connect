---
sidebar_position: 3
title: requestPermission
---

# `requestPermission`

Request permission for specified record types and access types.

# Method

```ts
requestPermission(permissions: Permission[], providerPackageName: string): Promise<Permission[]>
```

# Example

```ts
import { requestPermission } from 'react-native-health-connect';

const requestPermissions = () => {
  requestPermission([
    {
      accessType: 'read',
      recordType: 'activeCaloriesBurned',
    },
    {
      accessType: 'write',
      recordType: 'activeCaloriesBurned',
    },
  ]).then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```
