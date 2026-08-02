# Launching Health Connect contracts without touching MainActivity

## Context

Bare React Native consumers used to be required to edit `MainActivity`:

```kotlin
HealthConnectPermissionDelegate.setPermissionDelegate(this)
```

That existed purely because `ComponentActivity.registerForActivityResult` must be called **before
the activity reaches STARTED**. A native module is constructed far too late to do it itself, so the
library had to borrow the host's activity. Expo projects sidestepped this with a
`ReactActivityLifecycleListener` (`android-expo/.../HealthConnectPermissionReactActivityHandler.kt`);
bare RN has no equivalent hook.

Goal: remove the `MainActivity` requirement for both bare and Expo consumers.

## Approach that does NOT work: `startActivityForResult` + `ActivityEventListener`

The obvious idea is that `ActivityResultContract` is a plain object, decoupled from the launcher —
so you could call `contract.createIntent(...)` yourself, fire
`reactContext.startActivityForResult(...)`, and parse the result in an
`ActivityEventListener`.

**This crashes on Android 14+ (API 34+):**

```
android.content.ActivityNotFoundException: No Activity found to handle Intent
  { act=androidx.activity.result.contract.action.REQUEST_PERMISSIONS ... }
```

On U+, `PermissionController.createRequestPermissionResultContract(...)` delegates to
`ActivityResultContracts.RequestMultiplePermissions`, whose `createIntent` returns a **synthetic**
intent. Nothing in the system handles that action — only `ActivityResultRegistry` knows how to
service it. So the contract genuinely requires a registry, and this approach is a dead end.

Verified broken on an API 36 emulator.

## Implemented approach: a library-owned transparent activity

Host our own `ComponentActivity`, which comes with its own `ActivityResultRegistry`. Because
library manifests are merged into the consuming app automatically, this still requires **zero**
changes from consumers.

### Files

- **`android/src/main/java/dev/matinzd/healthconnect/permissions/HealthConnectPermissionActivity.kt`**
  - `ComponentActivity` that registers both contracts in `onCreate`
    (`PermissionController.createRequestPermissionResultContract(providerPackageName)` and
    `ExerciseRouteRequestContract()`) and launches immediately based on an intent extra.
  - The pending request is a `CompletableDeferred` held in the companion object
    (`setPendingRequest` / `takePendingRequest`), typed via `PendingHealthConnectRequest`. The
    result objects (`Set<String>`, `ExerciseRoute`) are handed back directly rather than
    re-encoded into an Intent — `ExerciseRoute` is not `Parcelable`.
  - `savedInstanceState != null` → do **not** re-launch; the registry replays the pending result
    to the re-registered launchers.
  - `onDestroy` completes the deferred with `emptySet()` / `null` when `isFinishing` and no result
    was delivered, so a dismissed or killed dialog can never leave the JS promise hanging.

- **`.../permissions/HealthConnectPermissionLauncher.kt`**
  - Owned by `HealthConnectManager`. Exposes `suspend requestPermissions(providerPackageName, permissions)`
    and `suspend requestExerciseRoute(recordId)`.
  - Starts `HealthConnectPermissionActivity` from `reactContext.currentActivity` (so it stacks in
    the same task), guards against concurrent requests, and awaits the deferred.

- **`android/src/main/AndroidManifest.xml`**
  ```xml
  <application>
    <activity
      android:name="dev.matinzd.healthconnect.permissions.HealthConnectPermissionActivity"
      android:excludeFromRecents="true"
      android:exported="false"
      android:theme="@android:style/Theme.Translucent.NoTitleBar" />
  </application>
  ```

- **`HealthConnectManager.kt`** — now stores `providerPackageName` from `initialize()` (it was
  previously discarded) because the permission contract needs it. Both call sites are wrapped in
  try/catch so new failure modes reject instead of hanging.

- **`utils/ExceptionsUtils.kt`** — added `ActivityNotAvailable` (`ACTIVITY_NOT_AVAILABLE`) and
  `RequestAlreadyInProgress` (`REQUEST_ALREADY_IN_PROGRESS`).

### Deprecations / cleanup

- `HealthConnectPermissionDelegate` is now a `@Deprecated` no-op, kept so existing `MainActivity`
  code still compiles. Remove in a future major.
- `android-expo`'s `HealthConnectPermissionReactActivityHandler` was deleted;
  `HealthConnectPackage` no longer contributes a lifecycle listener. The `android-expo` module now
  exists only as an empty `Package` and could be removed entirely — that touches the config plugin
  and CI linking assertions, so it was left in place.
- Setup instructions removed from `README.md` and `docs/docs/get-started.md`, and the
  `setPermissionDelegate` call removed from the bare example's `MainActivity.kt`.

## Trade-offs

- `startActivity` needs a live `currentActivity` → rejects with `ActivityNotAvailable`. The old
  launcher did not have this constraint.
- The pending deferred is static and in-memory, so it does not survive process death mid-dialog —
  same as the old `Channel`-based delegate, so no regression.

## Verification status

Done on an API 36 emulator with the bare example (no `setPermissionDelegate` anywhere):

- Library, bare example app, and Expo module all compile.
- Activity confirmed present in the app's merged `AndroidManifest.xml`.
- `initialize` → `{"result": true}`.
- `requestPermission` → real Health Connect consent screen → "Allow all" → granted list returned to
  JS. No crash.
- A subsequent request works, so the in-flight guard clears.

Still untested on device:

- Explicit deny / back-out of the consent dialog (Health Connect returned immediately once
  permissions were granted, so the path could not be driven). Covered in code by the `onDestroy`
  guard.
- `requestExerciseRoute` — needs an exercise session record that has a route.
