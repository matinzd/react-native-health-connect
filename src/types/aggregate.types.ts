interface ActiveCaloriesBurnedAggregateResult {
  recordType: 'activeCaloriesBurned';
  inCalories: number;
  inKilojoules: number;
  inKilocalories: number;
  inJoules: number;
}

interface BasalMetabolicRateAggregateResult {
  recordType: 'basalMetabolicRate';
  inCalories: number;
  inKilojoules: number;
  inKilocalories: number;
  inJoules: number;
}

interface BloodPressureAggregateResult {
  recordType: 'bloodPressure';
  SYSTOLIC_AVG: number;
  SYSTOLIC_MIN: number;
  DIASTOLIC_AVG: number;
  DIASTOLIC_MIN: number;
  DIASTOLIC_MAX: number;
}

export type AggregateRecordResult =
  | ActiveCaloriesBurnedAggregateResult
  | BasalMetabolicRateAggregateResult
  | BloodPressureAggregateResult;

export type AggregateResultRecordType = AggregateRecordResult['recordType'];

export type AggregateResult<T extends AggregateResultRecordType> = Omit<
  Extract<AggregateRecordResult, { recordType: T }>,
  'recordType'
>;

export interface AggregateRequest<T extends AggregateResultRecordType> {
  recordType: T;
  startTime: string;
  endTime: string;
  dataOriginFilter?: string[];
}
