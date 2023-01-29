---
sidebar_position: 3
title: Permissions
---

# Permissions

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
      // highlight-start
+     <intent-filter>
+       <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />
+     </intent-filter>
+     <meta-data android:name="health_permissions" android:resource="@array/health_permissions" />
      // highlight-end
    </activity>
```

- Create a new values resource file at `/res/values/health_permissions.xml` and add the necessary permissions:

```xml title="android/src/main/res/values/health_permissions" showLineNumbers
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

## Complete list of permissions

|Record class type     |Read permission declaration                           |Write permission declaration                           |
|----------------------|------------------------------------------------------|-------------------------------------------------------|
|ActiveCaloriesBurned  |androidx.health.permission.ActiveCaloriesBurned.READ  |androidx.health.permission.ActiveCaloriesBurned.WRITE  |
|BasalBodyTemperature  |androidx.health.permission.BasalBodyTemperature.READ  |androidx.health.permission.BasalBodyTemperature.WRITE  |
|BasalMetabolicRate    |androidx.health.permission.BasalMetabolicRate.READ    |androidx.health.permission.BasalMetabolicRate.WRITE    |
|BloodGlucose          |androidx.health.permission.BloodGlucose.READ          |androidx.health.permission.BloodGlucose.WRITE          |
|BloodPressure         |androidx.health.permission.BloodPressure.READ         |androidx.health.permission.BloodPressure.WRITE         |
|BodyFat               |androidx.health.permission.BodyFat.READ               |androidx.health.permission.BodyFat.WRITE               |
|BodyTemperature       |androidx.health.permission.BodyTemperature.READ       |androidx.health.permission.BodyTemperature.WRITE       |
|BoneMass              |androidx.health.permission.BoneMass.READ              |androidx.health.permission.BoneMass.WRITE              |
|CervicalMucus         |androidx.health.permission.CervicalMucus.READ         |androidx.health.permission.CervicalMucus.WRITE         |
|CyclingPedalingCadence|androidx.health.permission.CyclingPedalingCadence.READ|androidx.health.permission.CyclingPedalingCadence.WRITE|
|Distance              |androidx.health.permission.Distance.READ              |androidx.health.permission.Distance.WRITE              |
|ElevationGained       |androidx.health.permission.ElevationGained.READ       |androidx.health.permission.ElevationGained.WRITE       |
|ExerciseEvent         |androidx.health.permission.ExerciseSession.READ       |androidx.health.permission.ExerciseSession.WRITE       |
|ExerciseLap           |androidx.health.permission.ExerciseSession.READ       |androidx.health.permission.ExerciseSession.WRITE       |
|ExerciseRepetitions   |androidx.health.permission.ExerciseSession.READ       |androidx.health.permission.ExerciseSession.WRITE       |
|ExerciseSession       |androidx.health.permission.ExerciseSession.READ       |androidx.health.permission.ExerciseSession.WRITE       |
|FloorsClimbed         |androidx.health.permission.FloorsClimbed.READ         |androidx.health.permission.FloorsClimbed.WRITE         |
|HeartRate             |androidx.health.permission.HeartRate.READ             |androidx.health.permission.HeartRate.WRITE             |
|Height                |androidx.health.permission.Height.READ                |androidx.health.permission.Height.WRITE                |
|HipCircumference      |androidx.health.permission.HipCircumference.READ      |androidx.health.permission.HipCircumference.WRITE      |
|Hydration             |androidx.health.permission.Hydration.READ             |androidx.health.permission.Hydration.WRITE             |
|LeanBodyMass          |androidx.health.permission.LeanBodyMass.READ          |androidx.health.permission.LeanBodyMass.WRITE          |
|MenstruationFlow      |androidx.health.permission.MenstruationFlow.READ      |androidx.health.permission.MenstruationFlow.WRITE      |
|Nutrition             |androidx.health.permission.Nutrition.READ             |androidx.health.permission.Nutrition.WRITE             |
|OvulationTest         |androidx.health.permission.OvulationTest.READ         |androidx.health.permission.OvulationTest.WRITE         |
|OxygenSaturation      |androidx.health.permission.OxygenSaturation.READ      |androidx.health.permission.OxygenSaturation.WRITE      |
|Power                 |androidx.health.permission.Power.READ                 |androidx.health.permission.Power.WRITE                 |
|RespiratoryRate       |androidx.health.permission.RespiratoryRate.READ       |androidx.health.permission.RespiratoryRate.WRITE       |
|RestingHeartRate      |androidx.health.permission.RestingHeartRate.READ      |androidx.health.permission.RestingHeartRate.WRITE      |
|SexualActivity        |androidx.health.permission.SexualActivity.READ        |androidx.health.permission.SexualActivity.WRITE        |
|SleepSession          |androidx.health.permission.SleepSession.READ          |androidx.health.permission.SleepSession.WRITE          |
|SleepStage            |androidx.health.permission.SleepSession.READ          |androidx.health.permission.SleepSession.WRITE          |
|Speed                 |androidx.health.permission.Speed.READ                 |androidx.health.permission.Speed.WRITE                 |
|StepsCadence          |androidx.health.permission.StepsCadence.READ          |androidx.health.permission.StepsCadence.WRITE          |
|Steps                 |androidx.health.permission.Steps.READ                 |androidx.health.permission.Steps.WRITE                 |
|SwimmingStrokes       |androidx.health.permission.SwimmingStrokes.READ       |androidx.health.permission.SwimmingStrokes.WRITE       |
|TotalCaloriesBurned   |androidx.health.permission.TotalCaloriesBurned.READ   |androidx.health.permission.TotalCaloriesBurned.WRITE   |
|Vo2Max                |androidx.health.permission.Vo2Max.READ                |androidx.health.permission.Vo2Max.WRITE                |
|WaistCircumference    |androidx.health.permission.WaistCircumference.READ    |androidx.health.permission.WaistCircumference.WRITE    |
|Weight                |androidx.health.permission.Weight.READ                |androidx.health.permission.Weight.WRITE                |
|WheelchairPushes      |androidx.health.permission.WheelchairPushes.READ      |androidx.health.permission.WheelchairPushes.WRITE      |

You can read more about data types and permissions [here](https://developer.android.com/guide/health-and-fitness/health-connect/data-and-data-types/data-types).
