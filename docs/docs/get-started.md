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

## Setting up permissions

To access health data from the Health Connect app in your own app, you need to add the necessary permissions and filters to the app manifest.

- Add the following code inside the activity tag in `AndroidManifest.xml`:

```diff title="android/src/main/AndroidManifest.xml"
    <activity
      android:name=".MainActivity"
      android:label="@string/app_name"
      android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|screenSize|smallestScreenSize|uiMode"
      android:launchMode="singleTask"
      android:windowSoftInputMode="adjustResize"
      android:exported="true">
      <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
+     <intent-filter>
+       <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />
+     </intent-filter>
+     <meta-data android:name="health_permissions" android:resource="@array/health_permissions" />
    </activity>
```

- Create a new values resource file at `/res/values/health_permissions.xml` and add the necessary permissions:

```xml title="android/src/main/res/values/health_permissions"
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <array name="health_permissions">
    <item>androidx.health.permission.HeartRate.READ</item>
    <item>androidx.health.permission.HeartRate.WRITE</item>
    <item>androidx.health.permission.Steps.READ</item>
    <item>androidx.health.permission.Steps.WRITE</item>
  </array>
</resources>
```

## Features

- Typescript :white_check_mark:
- Supports both old and new architecture :white_check_mark:

## Tips

1. Keep in mind that this library is **Android only**.
1. Health Connect is now currently on alpha version. Check [here](https://developer.android.com/jetpack/androidx/releases/health-connect).
1. If the user declines your permission request twice, your app is permanently locked out, and cannot request permissions again.

Read more about health connect architecture [here](https://developer.android.com/guide/health-and-fitness/health-connect/platform-overview/architecture).

## License

MIT
