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

interface ActiveCaloriesBurnedRecordResult
  extends Omit<ActiveCaloriesBurnedRecord, 'energy'> {
  energy: EnergyResult;
}

interface BasalBodyTemperatureRecordResult
  extends Omit<BasalBodyTemperatureRecord, 'temperature'> {
  temperature: {
    inFahrenheit: number;
    inCelsius: number;
  };
}

interface BasalMetabolicRateRecordResult
  extends Omit<BasalMetabolicRateRecord, 'basalMetabolicRate'> {
  basalMetabolicRate: {
    inKilocaloriesPerDay: number;
    inWatts: number;
  };
}

interface BloodGlucoseRecordResult extends Omit<BloodGlucoseRecord, 'level'> {
  level: {
    inMillimolesPerLiter: number;
    inMilligramsPerDeciliter: number;
  };
}

interface BloodPressureRecordResult
  extends Omit<BloodPressureRecord, 'systolic' | 'diastolic'> {
  systolic: PressureResult;
  diastolic: PressureResult;
}

interface BodyTemperatureRecordResult
  extends Omit<BodyTemperatureRecord, 'temperature'> {
  temperature: {
    inFahrenheit: number;
    inCelsius: number;
  };
}

interface BodyFatRecordResult extends BodyFatRecord {}

interface BodyWaterMassRecordResult extends Omit<BodyWaterMassRecord, 'mass'> {
  mass: MassResult;
}

interface BoneMassRecordResult extends Omit<BoneMassRecord, 'mass'> {
  mass: MassResult;
}

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

interface DistanceRecordResult extends Omit<DistanceRecord, 'distance'> {
  distance: LengthResult;
}

interface HeightRecordResult extends Omit<HeightRecord, 'height'> {
  height: LengthResult;
}

interface HydrationRecordResult extends Omit<HydrationRecord, 'volume'> {
  volume: VolumeResult;
}

interface HeartRateVariabilityRmssdRecordResult
  extends HeartRateVariabilityRmssdRecord {}

interface SexualActivityRecordResult extends SexualActivityRecord {}

interface WeightRecordResult extends Omit<WeightRecord, 'weight'> {
  weight: MassResult;
}

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
