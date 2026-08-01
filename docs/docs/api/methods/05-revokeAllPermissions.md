---
sidebar_poition: 4
title: revokeAllPermissions
---

# `revokeAllPermissions`

Revokes all previously granted permissions by the user to the calling app.

:::caution The revocation does not take effect until the app restarts

This is a documented Health Connect platform limitation, not a bug in this library. After calling `revokeAllPermissions()`, the running process keeps behaving as if the permissions were still granted:

- [`getGrantedPermissions`](./04-getGrantedPermissions.md) keeps returning the revoked permissions.
- Reads and writes keep succeeding, and written data really is persisted.

The permissions are only actually revoked once the app process is restarted.

Because of this, do not use `revokeAllPermissions()` as the mechanism behind an in-app "disconnect from Health Connect" toggle — the user will turn it off and your app will keep syncing. Instead:

- Send the user to Health Connect to revoke there, using [`openHealthConnectSettings`](./14-openHealthConnectSettings.md).
- If you do call `revokeAllPermissions()`, tell the user the change takes effect after restarting the app.
- Track the "disconnected" state in your own app and stop reading and writing yourself, rather than relying on Health Connect to start rejecting your calls.

See Google's guidance on the ["Sync with Health Connect" toggle](https://developer.android.com/health-and-fitness/health-connect/ui/permissions#sync-with-health-connect).

:::

# Method

```ts
revokeAllPermissions(): Promise<void>
```

# Example

```ts
import { revokeAllPermissions } from 'react-native-health-connect';

// ...
revokeAllPermissions();
```
