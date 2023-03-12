import type {
  EnergyResult,
  LengthResult,
  MassResult,
  PressureResult,
  VolumeResult,
} from './base.types';
import type {
  ActiveCaloriesBurnedRecord,
  BasalBodyTemperatureRecord,
  BasalMetabolicRateRecord,
  BloodGlucoseRecord,
  BloodPressureRecord,
  BodyFatRecord,
  BodyTemperatureRecord,
  BodyWaterMassRecord,
  BoneMassRecord,
  CervicalMucusRecord,
  CyclingPedalingCadenceRecord,
  DistanceRecord,
  ElevationGainedRecord,
  ExerciseSessionRecord,
  FloorsClimbedRecord,
  HeartRateRecord,
  HeartRateVariabilityRmssdRecord,
  HeightRecord,
  HydrationRecord,
  RecordType,
  SexualActivityRecord,
  StepsRecord,
  WeightRecord,
} from './records.types';

type Identity<T> = { [P in keyof T]: T[P] };

type Replace<T, K extends keyof T, TReplace> = Identity<
  Pick<T, Exclude<keyof T, K>> & {
    [P in K]: TReplace;
  }
>;

interface ActiveCaloriesBurnedRecordResult
  extends Replace<ActiveCaloriesBurnedRecord, 'energy', EnergyResult> {}

interface BasalBodyTemperatureRecordResult
  extends Replace<
    BasalBodyTemperatureRecord,
    'temperature',
    {
      inFahrenheit: number;
      inCelsius: number;
    }
  > {}

interface BasalMetabolicRateRecordResult
  extends Replace<
    BasalMetabolicRateRecord,
    'basalMetabolicRate',
    {
      inKilocaloriesPerDay: number;
      inWatts: number;
    }
  > {}

interface BloodGlucoseRecordResult
  extends Replace<
    BloodGlucoseRecord,
    'level',
    {
      inMillimolesPerLiter: number;
      inMilligramsPerDeciliter: number;
    }
  > {}

interface BloodPressureRecordResult
  extends Omit<BloodPressureRecord, 'systolic' | 'diastolic'> {
  systolic: PressureResult;
  diastolic: PressureResult;
}

interface BodyTemperatureRecordResult
  extends Replace<
    BodyTemperatureRecord,
    'temperature',
    {
      inFahrenheit: number;
      inCelsius: number;
    }
  > {}

interface BodyFatRecordResult extends BodyFatRecord {}

interface BodyWaterMassRecordResult
  extends Replace<BodyWaterMassRecord, 'mass', MassResult> {}

interface BoneMassRecordResult
  extends Replace<BoneMassRecord, 'mass', MassResult> {}

interface CervicalMucusRecordResult extends CervicalMucusRecord {}

interface CyclingPedalingCadenceRecordResult
  extends CyclingPedalingCadenceRecord {}

interface ElevationGainedRecordResult
  extends Omit<ElevationGainedRecord, 'elevation'> {
  elevation: LengthResult;
}

interface ExerciseSessionRecordResult extends ExerciseSessionRecord {}

interface FloorsClimbedRecordResult extends FloorsClimbedRecord {}

interface HeartRateRecordResult extends HeartRateRecord {}

interface StepsRecordResult extends StepsRecord {}

interface DistanceRecordResult
  extends Replace<DistanceRecord, 'distance', LengthResult> {}

interface HeightRecordResult
  extends Replace<HeightRecord, 'height', LengthResult> {}

interface HydrationRecordResult
  extends Replace<HydrationRecord, 'volume', VolumeResult> {}

interface HeartRateVariabilityRmssdRecordResult
  extends HeartRateVariabilityRmssdRecord {}

interface SexualActivityRecordResult extends SexualActivityRecord {}

interface WeightRecordResult
  extends Replace<WeightRecord, 'weight', MassResult> {}

type HealthConnectRecordResult =
  | ActiveCaloriesBurnedRecordResult
  | BasalBodyTemperatureRecordResult
  | BasalMetabolicRateRecordResult
  | BloodGlucoseRecordResult
  | BloodPressureRecordResult
  | BodyFatRecordResult
  | BodyTemperatureRecordResult
  | BodyWaterMassRecordResult
  | BoneMassRecordResult
  | CervicalMucusRecordResult
  | ElevationGainedRecordResult
  | ExerciseSessionRecordResult
  | FloorsClimbedRecordResult
  | CyclingPedalingCadenceRecordResult
  | HeartRateRecordResult
  | StepsRecordResult
  | DistanceRecordResult
  | HeightRecordResult
  | HydrationRecordResult
  | HeartRateVariabilityRmssdRecordResult
  | SexualActivityRecordResult
  | WeightRecordResult
  | WeightRecordResult;

export type RecordResult<T extends RecordType> = Omit<
  Extract<HealthConnectRecordResult, { recordType: T }>,
  'recordType'
>;
