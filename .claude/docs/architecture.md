# Architecture

Android-only React Native library wrapping `androidx.health.connect:connect-client`. There is no
iOS implementation — `src/index.tsx` swaps in a proxy that throws
`PLATFORM_NOT_SUPPORTED_ERROR` on non-Android platforms.

## Layers

```
src/index.tsx            public JS API (thin; mostly re-exports the native module)
src/NativeHealthConnect.ts  TurboModule spec — codegen source of truth (codegenConfig.jsSrcsDir = src)
src/types/               all record / aggregate / result / changes typings
   ↓ bridge
android/src/{newarch,oldarch}/HealthConnectSpec.kt   two hand-written base classes, one per arch
android/.../HealthConnectModule.kt   @ReactMethod surface; every method just delegates
android/.../HealthConnectManager.kt  all real logic: client lifecycle, coroutines, promises
android/.../records/                 per-record-type adapters
android/.../permissions/             permission + exercise route dialogs
android/.../utils/                   registries, unit conversion, exception → error-code mapping
```

`HealthConnectModule` is deliberately empty of logic — it exists to satisfy the codegen'd spec.
Put behaviour in `HealthConnectManager`.

The old/new architecture split lives in `android/build.gradle` via `isNewArchitectureEnabled()`,
which picks `src/oldarch` or `src/newarch` as an extra source dir. Both files declare the same
abstract methods; if you add a native method you must update **both**, plus
`src/NativeHealthConnect.ts`.

## Record adapter pattern

Every Health Connect record type has a `ReactXxxRecord` class in `android/.../records/`
implementing `ReactHealthRecordImpl<T : Record>` — parse-from-JS, parse-to-JS, and the three
aggregate request/result shapes.

Dispatch is by three parallel maps in `utils/HealthConnectUtils.kt`:

| Map | Purpose |
| --- | --- |
| `reactRecordTypeToClassMap` | `"Steps"` → `StepsRecord::class` (permissions, deletes, reads) |
| `reactRecordTypeToReactClassMap` | `"Steps"` → `ReactStepsRecord::class.java` |
| `healthConnectClassToReactClassMap` | `StepsRecord::class.java` → `ReactStepsRecord::class.java` (used when parsing change feeds, where only the SDK class is known) |

`ReactHealthRecord` reflectively instantiates the adapter from those maps. A record type missing
from any one of them fails at runtime, not compile time — see
[adding-a-record-type.md](adding-a-record-type.md).

## Error handling

`Promise.rejectWithException` in `utils/ExceptionsUtils.kt` maps exception types to stable string
error codes consumed by `src/errors.ts`. New library-specific exceptions go in that file **and**
in the `when` block, otherwise they surface as `UNKNOWN_ERROR`.

## Permissions

Permission and exercise-route dialogs need an `ActivityResultRegistry`, which a native module does
not have. The library hosts its own transparent activity to get one — no `MainActivity` changes
required from consumers. See
[../plans/activity-result-without-main-activity.md](../plans/activity-result-without-main-activity.md); it also
documents the approach that looks obvious but crashes on Android 14+.
