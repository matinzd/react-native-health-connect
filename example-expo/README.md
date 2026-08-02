# Expo example (SDK 57)

Exercises the Expo path of `react-native-health-connect`: the bundled config plugin
([`../app.plugin.js`](../app.plugin.js)) and autolinking of the library in a managed project.

There is **no `MainActivity` edit anywhere in this app**, and none is Expo-specific: permission
dialogs are hosted by an activity the library declares in its own manifest, so bare React Native
apps need no edit either. The library ships no Expo native module.

## Running

This example is its own Yarn project rather than a workspace of the root. Yarn refuses to
materialise a `link:`/`portal:` symlink whose target is an *ancestor* of the project, and Expo
autolinking needs a real `node_modules` entry to discover the library — unlike React Native CLI
autolinking, it does not read `react-native.config.js`. The `postinstall` script here creates that
symlink after the link step.

```sh
cd example-expo
yarn install
npx expo prebuild --clean --platform android
npx expo run:android
```

`android/` is gitignored — regenerate it with `prebuild`.

## What to check after prebuild

- `./gradlew projects` lists `Project ':react-native-health-connect'`. Nothing named
  `*-expo` should appear — the library has no Expo native module.
- `android/app/src/main/AndroidManifest.xml` has a standalone
  `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` intent-filter and a
  `ViewPermissionUsageActivity` activity-alias. Running prebuild twice must not duplicate either.
