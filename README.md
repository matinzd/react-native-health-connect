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

- [Health Connect](https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=en&gl=US) needs to be installed on the user's device, However the goal is to have this app preinstalled on Android devices in the future.
- Health Connect API requires `minSdkVersion=26` (Android Oreo / 8.0).

## Installation

Install `react-native-health-connect` by running:

```bash
yarn add react-native-health-connect@latest
```

Since this module is Android-only, you do not need to run `pod install`.


## Expo installation

This package cannot be used in the [Expo Go](https://expo.io/client) app, but it can be used with custom managed apps.
Just add the [config plugin](https://docs.expo.io/guides/config-plugins/) to the [`plugins`](https://docs.expo.io/versions/latest/config/app/#plugins) array of your `app.json` or `app.config.js`:

First install the package with yarn, npm, or [`expo install`](https://docs.expo.io/workflow/expo-cli/#expo-install).

```sh
expo install react-native-health-connect
```

Then add the prebuild [config plugin](https://docs.expo.io/guides/config-plugins/) to the [`plugins`](https://docs.expo.io/versions/latest/config/app/#plugins) array of your `app.json` or `app.config.js`:

```json
{
  "expo": {
    "plugins": ["react-native-health-connect"]
  }
}
```

- Edit your app.json again and add this

```json
{
  "expo": {
    ...
    "plugins": [
      [
        "expo-build-properties",
        {
          "android": {
            "compileSdkVersion": 34,
            "targetSdkVersion": 34,
            "minSdkVersion": 26
          },
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

For iOS there are two alteranatives you can use which is very similar to Health Connect on Android. First one is [@kingstinct/react-native-healhkit](https://github.com/kingstinct/react-native-healthkit) and the other one is [react-native-health](https://github.com/agencyenterprise/react-native-health). These options are similar in functionality and can help you manage your health data on iOS.

## Documentation

More examples and full documentation can be found [here](https://matinzd.github.io/react-native-health-connect/)

## Features

- Typescript :white_check_mark:
- Supports both old and new architecture :white_check_mark:

## License

MIT
