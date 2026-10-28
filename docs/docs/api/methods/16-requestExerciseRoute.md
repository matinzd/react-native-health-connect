---
title: requestExerciseRoute
---

# `requestExerciseRoute`

Health Connect requires users' permission to access routes for exercise records ([see Android docs](https://developer.android.com/health-and-fitness/guides/health-connect/develop/exercise-routes)). When exercise records are fetched, they will include an `exerciseRoute` field with a `type` (and possibly a `route`). This method should be called to request permissions to fetch the route if the `type` is `"CONSENT_REQUIRED"`.

`type` is the string Health Connect returns (`"DATA"`, `"NO_DATA"`, or `"CONSENT_REQUIRED"`), not the numeric `ExerciseRouteResultType` enum. Route points on the read path use `LengthResult` (`inMeters`, `inKilometers`, …), not the write-side `Length` (`value` / `unit`).

NOTE: To read exercise routes, you need to declare the required permissions in your app's `AndroidManifest.xml`:

```xml
<application>
  <uses-permission
android:name="android.permission.health.READ_EXERCISE_ROUTES" />
  <uses-permission
android:name="android.permission.health.READ_EXERCISE" />
...
</application>
```

# Method

```ts
requestExerciseRoute(recordId: string): Promise<LocationResult[]>
```

# Example

```ts
import {
  requestExerciseRoute,
  readRecord,
} from "react-native-health-connect";

const recordId = "6bd8109d-349b-319a-890a-c5a20902b530";

readRecord("ExerciseSession", recordId)
  .then((exercise) => {
    console.log("Exercise record: ", JSON.stringify(exercise, null, 2));

    // Check if consent is required to read route:
    if (exercise.exerciseRoute?.type === "CONSENT_REQUIRED") {
      requestExerciseRoute(recordId).then((route) => {
        console.log(route[0]?.altitude?.inMeters);
      });
    }
  })
  .catch((err) => {
    console.error("Error reading exercise record", { err });
  });
  
```
