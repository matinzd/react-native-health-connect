---
title: getGrantedPermissions
---

# `getGrantedPermissions`

Returns a set of all health permissions granted by the user to the calling provider app.

:::caution Results are stale after calling `revokeAllPermissions`

If your app called [`revokeAllPermissions`](./05-revokeAllPermissions.md), this keeps returning the revoked permissions until the app process restarts. That is a Health Connect platform limitation — see [`revokeAllPermissions`](./05-revokeAllPermissions.md) for details and for what to do instead.

:::

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
