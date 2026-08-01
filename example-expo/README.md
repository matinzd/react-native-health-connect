# Expo example (SDK 57)

Exercises the Expo path of `react-native-health-connect`: the bundled config plugin and the
bundled Expo module in [`../android-expo`](../android-expo), which registers
`HealthConnectPermissionDelegate` from a `ReactActivityLifecycleListener`.

Note there is **no `MainActivity` edit anywhere in this app** — that is the point. If the Expo
module stops being autolinked, `requestPermission` fails with
`UninitializedPropertyAccessException`.

## Running

This example is deliberately **not** a Yarn workspace. Expo SDK 57 pins React Native 0.86 while
the bare `../example` is on 0.81, and hoisting both into one tree breaks Metro and Gradle. Install
it on its own:

```sh
cd example-expo
npm install
npx expo prebuild --clean --platform android
npx expo run:android
```

`android/` is gitignored — regenerate it with `prebuild`.

## What to check after prebuild

- `android/settings.gradle` includes **both** `react-native-health-connect` (React Native
  autolinking) and `react-native-health-connect-expo` (Expo autolinking). Both are required; see
  the `gradlePath` note in [`../expo-module.config.json`](../expo-module.config.json).
- The generated Expo package list contains `expo.modules.healthconnect.HealthConnectPackage`.
- `android/app/src/main/AndroidManifest.xml` has a standalone
  `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` intent-filter and a
  `ViewPermissionUsageActivity` activity-alias. Running prebuild twice must not duplicate either.
