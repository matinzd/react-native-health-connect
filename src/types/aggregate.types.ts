import type {
  EnergyResult,
  LengthResult,
  PressureResult,
  TimeRangeFilter,
  VolumeResult,
} from './base.types';

interface BaseAggregate {
  dataOrigins: string[];
}

interface ActiveCaloriesBurnedAggregateResult extends BaseAggregate {
  recordType: 'ActiveCaloriesBurned';
  ACTIVE_CALORIES_TOTAL: EnergyResult;
}

interface BasalMetabolicRateAggregateResult extends BaseAggregate {
  recordType: 'BasalMetabolicRate';
  BASAL_CALORIES_TOTAL: EnergyResult;
}

interface BloodPressureAggregateResult extends BaseAggregate {
  recordType: 'BloodPressure';
  SYSTOLIC_AVG: PressureResult;
  SYSTOLIC_MIN: PressureResult;
  DIASTOLIC_AVG: PressureResult;
  DIASTOLIC_MIN: PressureResult;
  DIASTOLIC_MAX: PressureResult;
}

interface ExerciseSessionAggregateResult extends BaseAggregate {
  recordType: 'ExerciseSession';
  EXERCISE_DURATION_TOTAL: {
    inSeconds: number;
  };
}

interface FloorsClimbedAggregateResult extends BaseAggregate {
  recordType: 'FloorsClimbed';
  FLOORS_CLIMBED_TOTAL: number;
}

interface CyclingPedalingCadenceAggregateResult extends BaseAggregate {
  recordType: 'CyclingPedalingCadence';
  RPM_AVG: number;
  RPM_MAX: number;
  RPM_MIN: number;
}

interface HeartRateAggregateResult extends BaseAggregate {
  recordType: 'HeartRate';
  BPM_AVG: number;
  BPM_MAX: number;
  BPM_MIN: number;
  MEASUREMENTS_COUNT: number;
}

interface StepsAggregateResult extends BaseAggregate {
  recordType: 'Steps';
  COUNT_TOTAL: number;
}

interface DistanceAggregateResult extends BaseAggregate {
  recordType: 'Distance';
  DISTANCE: LengthResult;
}

interface HeightAggregateResult extends BaseAggregate {
  recordType: 'Height';
  HEIGHT_AVG: LengthResult;
  HEIGHT_MIN: LengthResult;
  HEIGHT_MAX: LengthResult;
}

interface HydrationAggregateResult extends BaseAggregate {
  recordType: 'Hydration';
  VOLUME_TOTAL: VolumeResult;
}

export type AggregateRecordResult =
  | ActiveCaloriesBurnedAggregateResult
  | BasalMetabolicRateAggregateResult
  | BloodPressureAggregateResult
  | ExerciseSessionAggregateResult
  | FloorsClimbedAggregateResult
  | CyclingPedalingCadenceAggregateResult
  | HeartRateAggregateResult
  | StepsAggregateResult
  | DistanceAggregateResult
  | HeightAggregateResult
  | HydrationAggregateResult;

export type AggregateResultRecordType = AggregateRecordResult['recordType'];

export type AggregateResult<T extends AggregateResultRecordType> = Omit<
  Extract<AggregateRecordResult, { recordType: T }>,
  'recordType'
>;

export interface AggregateRequest<T extends AggregateResultRecordType> {
  recordType: T;
  timeRangeFilter: TimeRangeFilter;
  dataOriginFilter?: string[];
}
