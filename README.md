<div align="center">
  <img src="https://developer.android.com/static/guide/health-and-fitness/health-connect/images/health_connect_logo_192pxnew.png" />
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
- If you are planning to release your app on Google Play, you will need to submit a [declaration form](https://docs.google.com/forms/d/1LFjbq1MOCZySpP5eIVkoyzXTanpcGTYQH26lKcrQUJo/viewform?edit_requested=true). Approval can take up to 7 days.
- Approval does not grant you immediate access to Health Connect. A whitelist must propagate to the Health Connect servers, which take an additional 5-7 business days. The whitelist is updated every Monday according to Google Fit AHP support.

## Installation

To install react-native-health-connect, use the following command:

```bash
npm install react-native-health-connect
```

If you are using React Native CLI template, for version 2 onwards, please add the following code into your `MainActivity.kt` within the `onCreate` method:

```diff
package com.healthconnectexample

+ import android.os.Bundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
+ import dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate

class MainActivity : ReactActivity() {
  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "HealthConnectExample"

+ override fun onCreate(savedInstanceState: Bundle?) {
+   super.onCreate(savedInstanceState)
+   // In order to handle permission contract results, we need to set the permission delegate.
+   HealthConnectPermissionDelegate.setPermissionDelegate(this)
+ }

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
    DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}

```

You also need to setup permissions in your `AndroidManifest.xml` file. For more information, check [here](https://matinzd.github.io/react-native-health-connect/docs/permissions).

## Expo installation

This package cannot be used in the [Expo Go](https://expo.dev/go) app, but it works in a custom
development build. It ships its own [config plugin](https://docs.expo.dev/config-plugins/introduction/),
so installing the package is all you need:

```sh
npx expo install react-native-health-connect
```

Add the plugin and the permissions your app reads to `app.json`:

```json
{
  "expo": {
    "plugins": ["react-native-health-connect"],
    "android": {
      "permissions": [
        "android.permission.health.READ_STEPS",
        "android.permission.health.WRITE_STEPS"
      ]
    }
  }
}
```

Health Connect requires `minSdkVersion` 26. If your Expo SDK version does not already default to
that, or to a `compileSdkVersion` of 36, override them with
[`expo-build-properties`](https://docs.expo.dev/versions/latest/sdk/build-properties/):

```json
{
  "expo": {
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
  }
}
```

During [prebuild](https://expo.fyi/prebuilding) the plugin does the rest of the Android setup for
you:

- registers the permission delegate in `MainActivity`, without which `requestPermission()` throws
  `UninitializedPropertyAccessException`
- declares the permissions rationale intent filter Health Connect uses through Android 13
- declares the `ViewPermissionUsageActivity` alias Android 14+ requires, without which
  `requestPermission()` resolves with `[]` and no dialog is ever shown

(The library's own AndroidManifest already declares the package query for
`com.google.android.apps.healthdata`, so `getSdkStatus()` can see the standalone Health Connect
app on Android 13 and below without any further setup.)

Then rebuild the native app:

```sh
npx expo prebuild
npx expo run:android
```

Or build with EAS: `eas build --profile development --platform android`.

> `expo-health-connect` is a separate, older package that is no longer needed. The config plugin
> bundled with `react-native-health-connect` replaces it.

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
