# Expo integration and autolinking invariants

As of v4 the Expo integration ships **inside** this package; the separate `expo-health-connect`
is deprecated. The library has **no Expo native module** — the only Expo-specific artifact is the
config plugin.

| Piece | Role |
| --- | --- |
| `app.plugin.js` | Config plugin. Patches the consumer's `AndroidManifest.xml` with the `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` intent-filter (≤ Android 13) and a `ViewPermissionUsageActivity` activity-alias guarded by `START_VIEW_PERMISSION_USAGE` (Android 14+). Wrapped in `createRunOncePlugin`; both patches are idempotent — running prebuild twice must not duplicate them. |
| `android/` | Plain React Native library project. Expo autolinking picks it up by convention, same as any other RN package. |

There used to be an `android-expo/` Expo module plus an `expo-module.config.json`; it existed only
to register the permission delegate from a `ReactActivityLifecycleListener`. That requirement is
gone (see [../plans/activity-result-without-main-activity.md](../plans/activity-result-without-main-activity.md)),
so both were removed. Don't reintroduce an Expo module without a reason that genuinely needs
`expo-modules-core`.

## The invariants CI enforces

Both directions matter and each has a dedicated job in `.github/workflows/ci.yml`:

- **`expo-prebuild`** — in a managed Expo app, the config plugin must patch the manifest and
  `:react-native-health-connect` must be linked. Verified via `./gradlew projects`, not by grepping
  `settings.gradle` (`useExpoModules()` includes projects at configuration time, so there is
  nothing to grep), and then by `strings` over the APK's dex to prove packaging rather than mere
  configuration.
- **`bare-example`** — a bare React Native app must link the library and pull in **nothing** from
  expo. Bare projects have no `expo-modules-core`, so any expo dependency creeping into `android/`
  breaks every non-Expo consumer.

## example-expo is not a workspace

`example-expo/` is its own Yarn project. Yarn refuses to materialise a `link:`/`portal:` symlink
whose target is an *ancestor* directory, and Expo autolinking needs a real `node_modules` entry to
discover the library — unlike React Native CLI autolinking, it does not read
`react-native.config.js`. Its `postinstall` creates that symlink. Consequence: a root `yarn
install` does not reach it; run `yarn install` inside `example-expo/` separately. Its `android/`
directory is gitignored — regenerate with `npx expo prebuild --clean --platform android`.
