<div align="center">
  <a href="https://developer.android.com/guide/health-and-fitness/health-connect">
    <img src="https://developer.android.com/static/guide/health-and-fitness/health-connect/images/health_connect_logo_192pxnew.png"><br/>
    <h1>React Native Health Connect</h1>
  </a>
  <div align="center">
    <a href="http://www.npmjs.com/package/react-native-health-connect"><img src="https://img.shields.io/npm/v/react-native-health-connect.svg?style=for-the-badge&color=4284F3" /></a>
    <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" /></a>
  </div>
</div>

---

This library is a wrapper around Health Connect for react native. Health Connect is an Android API and platform. It unifies data from multiple devices and apps into an ecosystem. For Android developers, it provides a single interface for reading and writing a user’s health and fitness data. For Android users, it offers a place for control over which apps have read and/or write access to different types of data. Health Connect also provides on-device storage. Read more [here](https://developer.android.com/guide/health-and-fitness/health-connect).

## Requirements

- [Health Connect](https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=en&gl=US) needs to be installed on the user's device, However the goal is to have this app preinstalled on Android devices in the future.
- Health Connect API requires `mindSdkVersion=26` (Android Oreo / 8.0).

## Installation

Install `react-native-health-connect` by running:

```bash
yarn add react-native-health-connect
```

Since this module is Android-only, you do not need to run `pod install`.

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

## Documentation

More examples and full documentation can be found [here](https://matinzd.github.io/react-native-health-connect/)

## Features

- Typescript :white_check_mark:
- Supports both old and new architecture :white_check_mark:

## License

MIT
