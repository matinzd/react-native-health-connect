---
sidebar_position: 2
title: Get started
---

## Requirements

- [Health Connect](https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=en&gl=US) needs to be installed on the user's device, However the goal is to have this app preinstalled on Android devices in the future.
- Health Connect API requires `mindSdkVersion=26` (Android Oreo / 8.0).

:::note
Health Connect does not appear on the Home screen by default. To open Health Connect, go to Settings > Apps > Health Connect, or add Health Connect to your Quick Settings menu.
:::

:::note
Health Connect requires the user to have screen lock enabled with a PIN, pattern, or password so that the health data being stored within Health connect is protected from malicious parties while the device is locked. Please go to Settings > Security > Screen lock to set a screen lock.
:::

## Installation

1. Install react-native-health-connect by running:  
```bash
yarn add react-native-health-connect
```
Since this module is Android-only, you do not need to run `pod install`.
