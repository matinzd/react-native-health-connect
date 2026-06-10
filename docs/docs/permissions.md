---
sidebar_position: 3
title: Permissions
---

# Permissions

## Setting up permissions in React Native CLI template

To access health data from the Health Connect app in your own app, you need to add the necessary permissions and filters to the app manifest.

- Add the necessary permissions to `AndroidManifest.xml`:

```diff title="android/src/main/AndroidManifest.xml"
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.INTERNET" />
+   <uses-permission android:name="android.permission.health.READ_HEART_RATE"/>
+   <uses-permission android:name="android.permission.health.WRITE_HEART_RATE"/>
+   <uses-permission android:name="android.permission.health.READ_STEPS"/>
+   <uses-permission android:name="android.permission.health.WRITE_STEPS"/>
</manifest>
```

- Create `PermissionRationaleActivity.kt`

```diff title="android/app/src/main/java/com/healthconnectexample/PermissionRationaleActivity.kt"
package com.healthconnectexample

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class PermissionsRationaleActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val webView = WebView(this)
    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
      }
    }

    webView.loadUrl("https://developer.android.com/health-and-fitness/guides/health-connect/develop/get-started")

    setContentView(webView)
  }
}
```

- Add the following highlighted code inside the application tag as well:

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
+      <!-- For supported versions through Android 13, create an activity to show the rationale
+           of Health Connect permissions once users click the privacy policy link. -->
+      <intent-filter>
+        <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />
+      </intent-filter>
    </activity>

+    <activity
+      android:name=".PermissionsRationaleActivity"
+      android:exported="true">
+      <intent-filter>
+        <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />
+      </intent-filter>
+    </activity>
+
+    <!-- For versions starting Android 14, create an activity alias to show the rationale
+         of Health Connect permissions once users click the privacy policy link. -->
+    <activity-alias
+      android:name="ViewPermissionUsageActivity"
+      android:exported="true"
+      android:targetActivity=".MainActivity"
+      android:permission="android.permission.START_VIEW_PERMISSION_USAGE">
+      <intent-filter>
+        <action android:name="android.intent.action.VIEW_PERMISSION_USAGE" />
+        <category android:name="android.intent.category.HEALTH_PERMISSIONS" />
+      </intent-filter>
+    </activity-alias>
```


## Setting up permissions in Expo

You will need to use [EAS Build](https://docs.expo.dev/eas/) and [Config plugins](https://docs.expo.dev/config-plugins/introduction/) in your project.

- Edit `app.json` and add the permissions you need.

```json
{
  "expo": {
    ...
    "android": {
      ...
      "permissions": [
        "android.permission.health.READ_STEPS",
        "android.permission.health.WRITE_STEPS",
        "android.permission.health.READ_ACTIVE_CALORIES_BURNED"
      ]
    },
   ...
  }
}
```

## Complete list of permissions

| Record class type      | Read permission declaration                           | Write permission declaration                           |
| ---------------------- | ----------------------------------------------------- | ------------------------------------------------------ |
| ActiveCaloriesBurned   | android.permission.health.READ_ACTIVE_CALORIES_BURNED | android.permission.health.WRITE_ACTIVE_CALORIES_BURNED |
| BasalBodyTemperature   | android.permission.health.READ_BASAL_BODY_TEMPERATURE | android.permission.health.WRITE_BASAL_BODY_TEMPERATURE |
| BasalMetabolicRate     | android.permission.health.READ_BASAL_METABOLIC_RATE   | android.permission.health.WRITE_BASAL_METABOLIC_RATE   |
| BloodGlucose           | android.permission.health.READ_BLOOD_GLUCOSE          | android.permission.health.WRITE_BLOOD_GLUCOSE          |
| BloodPressure          | android.permission.health.READ_BLOOD_PRESSURE         | android.permission.health.WRITE_BLOOD_PRESSURE         |
| BodyFat                | android.permission.health.READ_BODY_FAT               | android.permission.health.WRITE_BODY_FAT               |
| BodyTemperature        | android.permission.health.READ_BODY_TEMPERATURE       | android.permission.health.WRITE_BODY_TEMPERATURE       |
| BoneMass               | android.permission.health.READ_BONE_MASS              | android.permission.health.WRITE_BONE_MASS              |
| CervicalMucus          | android.permission.health.READ_CERVICAL_MUCUS         | android.permission.health.WRITE_CERVICAL_MUCUS         |
| CyclingPedalingCadence | android.permission.health.READ_EXERCISE               | android.permission.health.WRITE_EXERCISE               |
| Distance               | android.permission.health.READ_DISTANCE               | android.permission.health.WRITE_DISTANCE               |
| ElevationGained        | android.permission.health.READ_ELEVATION_GAINED       | android.permission.health.WRITE_ELEVATION_GAINED       |
| ExerciseSession        | android.permission.health.READ_EXERCISE               | android.permission.health.WRITE_EXERCISE               |
| FloorsClimbed          | android.permission.health.READ_FLOORS_CLIMBED         | android.permission.health.WRITE_FLOORS_CLIMBED         |
| HeartRate              | android.permission.health.READ_HEART_RATE             | android.permission.health.WRITE_HEART_RATE             |
| Height                 | android.permission.health.READ_HEIGHT                 | android.permission.health.WRITE_HEIGHT                 |
| Hydration              | android.permission.health.READ_HYDRATION              | android.permission.health.WRITE_HYDRATION              |
| LeanBodyMass           | android.permission.health.READ_LEAN_BODY_MASS         | android.permission.health.WRITE_LEAN_BODY_MASS         |
| MenstruationFlow       | android.permission.health.READ_MENSTRUATION           | android.permission.health.WRITE_MENSTRUATION           |
| MenstruationPeriod     | android.permission.health.READ_MENSTRUATION           | android.permission.health.WRITE_MENSTRUATION           |
| Nutrition              | android.permission.health.READ_NUTRITION              | android.permission.health.WRITE_NUTRITION              |
| OvulationTest          | android.permission.health.READ_OVULATION_TEST         | android.permission.health.WRITE_OVULATION_TEST         |
| OxygenSaturation       | android.permission.health.READ_OXYGEN_SATURATION      | android.permission.health.WRITE_OXYGEN_SATURATION      |
| Power                  | android.permission.health.READ_POWER                  | android.permission.health.WRITE_POWER                  |
| RespiratoryRate        | android.permission.health.READ_RESPIRATORY_RATE       | android.permission.health.WRITE_RESPIRATORY_RATE       |
| RestingHeartRate       | android.permission.health.READ_RESTING_HEART_RATE     | android.permission.health.WRITE_RESTING_HEART_RATE     |
| SexualActivity         | android.permission.health.READ_SEXUAL_ACTIVITY        | android.permission.health.WRITE_SEXUAL_ACTIVITY        |
| SleepSession           | android.permission.health.READ_SLEEP                  | android.permission.health.WRITE_SLEEP                  |
| Speed                  | android.permission.health.READ_SPEED                  | android.permission.health.WRITE_SPEED                  |
| StepsCadence           | android.permission.health.READ_STEPS                  | android.permission.health.WRITE_STEPS                  |
| Steps                  | android.permission.health.READ_STEPS                  | android.permission.health.WRITE_STEPS                  |
| TotalCaloriesBurned    | android.permission.health.READ_TOTAL_CALORIES_BURNED  | android.permission.health.WRITE_TOTAL_CALORIES_BURNED  |
| Vo2Max                 | android.permission.health.READ_VO2_MAX                | android.permission.health.WRITE_VO2_MAX                |
| Weight                 | android.permission.health.READ_WEIGHT                 | android.permission.health.WRITE_WEIGHT                 |
| WheelchairPushes       | android.permission.health.READ_WHEELCHAIR_PUSHES      | android.permission.health.WRITE_WHEELCHAIR_PUSHES      |
| WriteExerciseRoute       | android.permission.health.WRITE_EXERCISE_ROUTE      | N/A      |

You can read more about data types and permissions [here](https://developer.android.com/guide/health-and-fitness/health-connect/data-and-data-types/data-types).

## Special Permissions

In addition to the standard record type permissions, Health Connect provides special permissions for specific functionality:

### Background Access Permission

This permission allows your app to read health data in the background, even when your app is not in the foreground.

First, add the background access permission to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.health.READ_HEALTH_DATA_IN_BACKGROUND"/>
```

Then, request the permission in your app:

```ts
// Request background access permission
requestPermission([
  {
    accessType: 'read',
    recordType: 'BackgroundAccessPermission',
  },
  // Other permissions...
]);
```

Under the hood, this maps to `HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND` in the Android Health Connect API.

See the [Background Access Permission](./api/methods/17-backgroundAccessPermission.md) documentation for more details.

### Exercise Route Permission

This special permission is required to write exercise routes:

```ts
requestPermission([
  {
    accessType: 'write',
    recordType: 'ExerciseRoute',
  },
  // Other permissions...
]);
```
