<div align="center">
  <a href="https://developer.android.com/guide/health-and-fitness/health-connect">
    <img src="https://developer.android.com/static/guide/health-and-fitness/health-connect/images/health_connect_logo_192pxnew.png"><br/>
  </a>
  <h1 align="center">React Native Health Connect</h1>
</div>

This library is a wrapper around Health Connect for react native. Health Connect is an Android API and platform. It unifies data from multiple devices and apps into an ecosystem. For Android developers, it provides a single interface for reading and writing a user’s health and fitness data. For Android users, it offers a place for control over which apps have read and/or write access to different types of data. Health Connect also provides on-device storage.
https://developer.android.com/guide/health-and-fitness/health-connect   

# Get started

## Requirements
- [Health Connect App](https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata&hl=en&gl=US) needs to be installed on the user's device, However the goal is to have this app preinstalled on Android devices in the future.
- Health Connect API requires `mindSdkVersion=26` (Android Oreo / 8.0).

## Installation

1. Install react-native-health-connect by running:   
`yarn add react-native-health-connect`

Since this module is Android-only, you do not need to run `pod install`.

## Setting up permissions

To access health data from the Health Connect app in your own app, you need to add the necessary permissions and filters to the app manifest.

- Add the following code inside the activity tag in `AndroidManifest.xml`:
```patch
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
```xml
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
