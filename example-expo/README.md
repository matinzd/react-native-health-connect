# Expo example (SDK 57)

Exercises the Expo path of `react-native-health-connect`: the bundled config plugin and the
bundled Expo module in [`../android-expo`](../android-expo).

Note there is **no `MainActivity` edit anywhere in this app**. That is no longer Expo-specific —
permission dialogs are hosted by an activity the library declares in its own manifest, so bare
React Native apps need no edit either. The Expo module now only exists for the config plugin's
autolinking path.

## Running

This example is its own Yarn project rather than a workspace of the root. Yarn refuses to
materialise a `link:`/`portal:` symlink whose target is an *ancestor* of the project, and Expo
autolinking needs a real `node_modules` entry to discover `../android-expo` — unlike React Native
CLI autolinking, it does not read `react-native.config.js`. The `postinstall` script here creates
that symlink after the link step.

```sh
cd example-expo
yarn install
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
