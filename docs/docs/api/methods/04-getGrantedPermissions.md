---
title: getGrantedPermissions
---

# `getGrantedPermissions`

Returns a set of all health permissions granted by the user to the calling provider app.

# Method

```ts
getGrantedPermissions(): Promise<Permission[]>
```

# Example

```ts
import { getGrantedPermissions } from 'react-native-health-connect';

const readGrantedPermissions = () => {
  getGrantedPermissions().then((permissions) => {
    console.log('Granted permissions ', { permissions });
  });
};
```
