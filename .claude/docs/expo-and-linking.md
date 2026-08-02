# Expo integration and autolinking invariants

As of v4 the Expo integration ships **inside** this package; the separate `expo-health-connect`
is deprecated. Three pieces are involved:

| Piece | Role |
| --- | --- |
| `app.plugin.js` | Config plugin. Patches the consumer's `AndroidManifest.xml` with the `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` intent-filter (≤ Android 13) and a `ViewPermissionUsageActivity` activity-alias guarded by `START_VIEW_PERMISSION_USAGE` (Android 14+). Wrapped in `createRunOncePlugin`; both patches are idempotent — running prebuild twice must not duplicate them. |
| `expo-module.config.json` | Points Expo autolinking at `android-expo/` with an explicit `gradlePath`. |
| `android-expo/` | An Expo `Package`. Historically registered the permission delegate via a `ReactActivityLifecycleListener`; that is gone, so it now contributes nothing and could be removed entirely (blocked only on the config-plugin/CI churn). |

## The invariants CI enforces

Both directions matter and each has a dedicated job in `.github/workflows/ci.yml`:

- **`expo-prebuild`** — in an Expo app, `:react-native-health-connect` **and**
  `:react-native-health-connect-expo` must both be linked. The `gradlePath` guard in
  `expo-module.config.json` is what keeps the former alive; if it regresses the main project
  silently disappears. Verified via `./gradlew projects`, not by grepping `settings.gradle`
  (`useExpoModules()` includes projects at configuration time, so there is nothing to grep), and
  then by `strings` over the APK's dex to prove packaging rather than mere configuration.
- **`bare-example`** — in a bare React Native app, `android-expo/` must **never** be linked. It
  depends on `expo-modules-core`, which bare projects do not have, so linking it breaks every
  non-Expo consumer.

## example-expo is not a workspace

`example-expo/` is its own Yarn project. Yarn refuses to materialise a `link:`/`portal:` symlink
whose target is an *ancestor* directory, and Expo autolinking needs a real `node_modules` entry to
discover `android-expo/` — unlike React Native CLI autolinking, it does not read
`react-native.config.js`. Its `postinstall` creates that symlink. Consequence: a root `yarn
install` does not reach it; run `yarn install` inside `example-expo/` separately. Its `android/`
directory is gitignored — regenerate with `npx expo prebuild --clean --platform android`.
