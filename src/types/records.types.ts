import type {
  Energy,
  InstantaneousRecord,
  IntervalRecord,
  BloodGlucose,
  Power,
  Pressure,
  Temperature,
  TimeRangeFilter,
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

export type HealthConnectRecord =
  | ActiveCaloriesBurnedRecord
  | BasalBodyTemperatureRecord
  | BasalMetabolicRateRecord
  | BloodGlucoseRecord
  | BloodPressureRecord;

export type RecordType = HealthConnectRecord['recordType'];

export interface ReadRecordsOptions {
  timeRangeFilter: TimeRangeFilter;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
}
