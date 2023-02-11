import type {
  Energy,
  InstantaneousRecord,
  IntervalRecord,
  BloodGlucose,
  Power,
  Pressure,
  Temperature,
  TimeRangeFilter,
  Mass,
  Length,
  CyclingPedalingCadenceSample,
} from './base.types';

export interface ActiveCaloriesBurnedRecord extends IntervalRecord {
  recordType: 'ActiveCaloriesBurned';
  energy: Energy;
}

export interface BasalBodyTemperatureRecord extends InstantaneousRecord {
  recordType: 'BasalBodyTemperature';
  temperature: Temperature;
  measurementLocation?: number;
}

export interface BasalMetabolicRateRecord extends InstantaneousRecord {
  recordType: 'BasalMetabolicRate';
  basalMetabolicRate: Power;
}

export interface BloodGlucoseRecord extends InstantaneousRecord {
  recordType: 'BloodGlucose';
  level: BloodGlucose;
  specimenSource: number;
  mealType: number;
  relationToMeal: number;
}

export interface BloodPressureRecord extends InstantaneousRecord {
  recordType: 'BloodPressure';
  systolic: Pressure;
  diastolic: Pressure;
  bodyPosition: number;
  measurementLocation: number;
}

export interface BodyFatRecord extends InstantaneousRecord {
  recordType: 'BodyFat';
  percentage: number;
}

export interface BodyTemperatureRecord extends InstantaneousRecord {
  recordType: 'BodyTemperature';
  temperature: Temperature;
  measurementLocation?: number;
}

export interface BodyWaterMassRecord extends InstantaneousRecord {
  recordType: 'BodyWaterMass';
  mass: Mass;
}

export interface BoneMassRecord extends InstantaneousRecord {
  recordType: 'BoneMass';
  mass: Mass;
}

export interface CervicalMucusRecord extends InstantaneousRecord {
  recordType: 'CervicalMucus';
  appearance?: number;
  sensation?: number;
}

export interface CyclingPedalingCadenceRecord extends IntervalRecord {
  recordType: 'CyclingPedalingCadence';
  samples: CyclingPedalingCadenceSample[];
}

export interface ElevationGainedRecord extends IntervalRecord {
  recordType: 'ElevationGained';
  elevation: Length;
}

export interface ExerciseSessionRecord extends IntervalRecord {
  recordType: 'ExerciseSession';
  exerciseType: number;
  title?: string;
  notes?: string;
}

export interface FloorsClimbedRecord extends IntervalRecord {
  recordType: 'FloorsClimbed';
  floors: number;
}

export type HealthConnectRecord =
  | ActiveCaloriesBurnedRecord
  | BasalBodyTemperatureRecord
  | BasalMetabolicRateRecord
  | BloodGlucoseRecord
  | BloodPressureRecord
  | BodyFatRecord
  | BodyTemperatureRecord
  | BodyWaterMassRecord
  | BoneMassRecord
  | CervicalMucusRecord
  | ElevationGainedRecord
  | ExerciseSessionRecord
  | FloorsClimbedRecord
  | CyclingPedalingCadenceRecord;

export type RecordType = HealthConnectRecord['recordType'];

export interface ReadRecordsOptions {
  timeRangeFilter: TimeRangeFilter;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
}
