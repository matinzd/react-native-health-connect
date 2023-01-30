import type {
  EnergyResult,
  PressureResult,
  TimeRangeFilter,
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

export type AggregateRecordResult =
  | ActiveCaloriesBurnedAggregateResult
  | BasalMetabolicRateAggregateResult
  | BloodPressureAggregateResult
  | ExerciseSessionAggregateResult;

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
