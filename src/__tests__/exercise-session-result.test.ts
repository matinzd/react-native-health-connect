import type { requestExerciseRoute } from '../index';
import type { LengthResult, Location } from '../types/base.types';
import type { RecordResult } from '../types/results.types';

type ExerciseSessionResult = RecordResult<'ExerciseSession'>;
type ExerciseRouteResult = NonNullable<ExerciseSessionResult['exerciseRoute']>;
type ExerciseLapResult = NonNullable<ExerciseSessionResult['laps']>[number];
type RequestedRoute = Awaited<ReturnType<typeof requestExerciseRoute>>;

// The shape native `lengthToJsMap` returns (HealthConnectUtils.kt).
const length: LengthResult = {
  inMeters: 12.5,
  inKilometers: 0.0125,
  inMiles: 0.007767,
  inInches: 492.126,
  inFeet: 41.0105,
};

// The type assertions in this file run in `yarn typecheck`.
describe('ExerciseSession read types', () => {
  it('types route points and laps with LengthResult', () => {
    const exerciseRoute: ExerciseRouteResult = {
      type: 'CONSENT_REQUIRED',
      route: [
        {
          time: '2020-01-01T00:00:00Z',
          latitude: 37.78,
          longitude: -122.4,
          horizontalAccuracy: length,
          verticalAccuracy: length,
          altitude: length,
        },
      ],
    };
    const lap: ExerciseLapResult = {
      startTime: '2020-01-01T00:00:00Z',
      endTime: '2020-01-01T00:10:00Z',
      length,
    };
    // requestExerciseRoute resolves the same parseExerciseRoute shape
    const requestedRoute: RequestedRoute = [
      {
        time: '2020-01-01T00:00:00Z',
        latitude: 37.78,
        longitude: -122.4,
        altitude: length,
      },
    ];

    expect(exerciseRoute.route[0]?.altitude?.inMeters).toBe(12.5);
    expect(lap.length.inMeters).toBe(12.5);
    expect(requestedRoute[0]?.altitude?.inMeters).toBe(12.5);
  });
});

export const writeLocation: Location = {
  time: '2020-01-01T00:00:00Z',
  latitude: 0,
  longitude: 0,
  // @ts-expect-error write-side Location keeps Length ({ value, unit })
  altitude: length,
};
