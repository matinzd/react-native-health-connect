<div align="center">
  <img src="https://developer.android.com/static/images/health-connect/health-connect.svg" />
  <div align="center">
    <h1>React Native Health Connect</h1>
  </div>
  <div align="center">
    <a href="https://www.npmjs.com/package/react-native-health-connect">
      <img src="https://img.shields.io/npm/v/react-native-health-connect.svg?style=for-the-badge&color=4284F3" />
    </a>
    <a href="https://opensource.org/licenses/MIT">
      <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" />
    </a>
  </div>
</div>

---

This library is a wrapper around Health Connect for react native. Health Connect is an Android API and platform. It unifies data from multiple devices and apps into an ecosystem. For Android developers, it provides a single interface for reading and writing a user’s health and fitness data. For Android users, it offers a place for control over which apps have read and/or write access to different types of data. Health Connect also provides on-device storage. Read more [here](https://developer.android.com/guide/health-and-fitness/health-connect).

## Requirements

Make sure you have React Native version 0.71 or higher **with the latest patch** installed to use v2 of React Native Health Connect.

- [Health Connect](https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=en&gl=US) needs to be installed on the user's device. Starting from Android 14 (Upside Down Cake), Health Connect is part of the Android Framework. Read more [here](https://developer.android.com/health-and-fitness/guides/health-connect/develop/get-started#step-1).
- Health Connect API requires `minSdkVersion=26` (Android Oreo / 8.0).
- If you are planning to release your app on Google Play, you must declare your Health Connect data access. This is now done in the [Play Console](https://play.google.com/apps/publish/signup) — the standalone Google Form previously used for these requests is being retired on September 3rd, 2026, and all submissions go through the Play Console instead. See Google's [health apps declaration guidance](https://support.google.com/googleplay/android-developer/answer/14738291?hl=en), or [how to sign up for a Play Console developer account](https://support.google.com/googleplay/android-developer/answer/6112435?hl=en#zippy=%2Cstep-sign-up-for-a-play-console-developer-account) if you do not have one yet. Approval can take up to 7 days.
- Approval does not grant you immediate access to Health Connect. A whitelist must propagate to the Health Connect servers, which take an additional 5-7 business days. The whitelist is updated every Monday according to Google Fit AHP support.

## Installation

To install react-native-health-connect, use the following command:

```bash
npm install react-native-health-connect
```

No `MainActivity` changes are required. Permission dialogs are handled entirely inside the library.

> If you are upgrading from an earlier version, you can now remove the
> `HealthConnectPermissionDelegate.setPermissionDelegate(this)` call from your `MainActivity`. It is
> deprecated and does nothing.

You also need to setup permissions in your `AndroidManifest.xml` file. For more information, check [here](https://matinzd.github.io/react-native-health-connect/docs/permissions).

## Expo installation

This package cannot be used in the [Expo Go](https://expo.io/client) app, but it can be used with custom managed apps.

> **As of v4, the Expo integration ships inside `react-native-health-connect`.** There is no longer
> a separate `expo-health-connect` package to install — see [Migrating from
> `expo-health-connect`](#migrating-from-expo-health-connect).

Install the package with yarn, npm, or [`expo install`](https://docs.expo.io/workflow/expo-cli/#expo-install):

```sh
npm install react-native-health-connect
npm install expo-build-properties --save-dev
```

Then add the [config plugin](https://docs.expo.io/guides/config-plugins/) to the [`plugins`](https://docs.expo.io/versions/latest/config/app/#plugins) array of your `app.json` or `app.config.js`:

```json
{
  "expo": {
    ...
    "plugins": [
      "react-native-health-connect",
      [
        "expo-build-properties",
        {
          "android": {
            "compileSdkVersion": 36,
            "targetSdkVersion": 36,
            "minSdkVersion": 26
          }
        }
      ]
    ]
   ...
  }
}
```

Then rebuild the native app:

- Run `expo prebuild`
  - This will apply the config plugin using [prebuilding](https://expo.fyi/prebuilding).
- Rebuild the app
  - `yarn android` -- Build on Android.

> If the project doesn't build correctly with `yarn android`, please file an issue and try setting the project up manually.

Finally create a new EAS development build

`eas build --profile development --platform android`

A complete working Expo app lives in [`example-expo/`](./example-expo).

### Migrating from `expo-health-connect`

`expo-health-connect` is deprecated; everything it did is now part of this package.

1. Remove it: `npm uninstall expo-health-connect`
2. In your `app.json` `plugins` array, replace `"expo-health-connect"` with `"react-native-health-connect"`.
3. Re-run `npx expo prebuild --clean`.

If you leave `expo-health-connect` installed, the Android build fails with a duplicate
`expo.modules.healthconnect.HealthConnectPackage` class — both packages contribute the same Kotlin
class. Removing the old package resolves it.

## Example

A quick example at a glance:

```ts
import {
  initialize,
  requestPermission,
  readRecords,
} from 'react-native-health-connect';

const readSampleData = async () => {
  // initialize the client
  const isInitialized = await initialize();

  // request permissions
  const grantedPermissions = await requestPermission([
    { accessType: 'read', recordType: 'ActiveCaloriesBurned' },
  ]);

  // check if granted

  const result = await readRecords('ActiveCaloriesBurned', {
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T12:00:00.405Z',
      endTime: '2023-01-09T23:53:15.405Z',
    },
  });
  // {
  //   result: [
  //     {
  //       startTime: '2023-01-09T12:00:00.405Z',
  //       endTime: '2023-01-09T23:53:15.405Z',
  //       energy: {
  //         inCalories: 15000000,
  //         inJoules: 62760000.00989097,
  //         inKilojoules: 62760.00000989097,
  //         inKilocalories: 15000,
  //       },
  //       metadata: {
  //         id: '239a8cfd-990d-42fc-bffc-c494b829e8e1',
  //         lastModifiedTime: '2023-01-17T21:06:23.335Z',
  //         clientRecordId: null,
  //         dataOrigin: 'com.healthconnectexample',
  //         clientRecordVersion: 0,
  //         device: 0,
  //       },
  //     },
  //   ],
  // }
};
```

## Alternatives

For iOS there are two alteranatives you can use which is very similar to Health Connect on Android. First one is [@kingstinct/react-native-healthkit](https://github.com/kingstinct/react-native-healthkit) and the other one is [react-native-health](https://github.com/agencyenterprise/react-native-health). These options are similar in functionality and can help you manage your health data on iOS.

## Documentation

More examples and full documentation can be found [here](https://matinzd.github.io/react-native-health-connect/)

## Features

- Typescript :white_check_mark:
- Supports both old and new architecture :white_check_mark:

## License

MIT
