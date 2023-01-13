interface Energy {
  value: number;
  unit: 'calories' | 'joules' | 'kilocalories' | 'kilojoules';
}

interface BloodGlucose {
  value: number;
  unit: 'milligramsPerDeciliter' | 'millimolesPerLiter';
}

interface Power {
  value: number;
  unit: 'watts' | 'kilocaloriesPerDay';
}

interface Temperature {
  value: number;
  unit: 'celsius' | 'fahrenheit';
}

interface BaseRecord {
  metadata?: Metadata;
}

interface InstantaneousRecord extends BaseRecord {
  time: string;
  zoneOffset?: string;
}

interface IntervalRecord extends BaseRecord {
  startTime: string;
  startZoneOffset?: string;
  endTime: string;
  endZoneOffset?: string;
}

interface Metadata {
  id: string;
  // package name of the app that created the record
  dataOrigin: string;
  lastModifiedTime: string;
  clientRecordId?: string;
  clientRecordVersion: number;
  // see: https://developer.android.com/reference/kotlin/androidx/health/connect/client/records/metadata/Device
  device: number;
}

interface ActiveCaloriesBurnedRecord extends IntervalRecord {
  recordType: 'activeCaloriesBurned';
  energy: Energy;
}

interface BasalBodyTemperatureRecord extends InstantaneousRecord {
  recordType: 'basalBodyTemperature';
  temperature: Temperature;
  measurementLocation: number;
}

interface BasalMetabolicRateRecord extends InstantaneousRecord {
  recordType: 'basalMetabolicRate';
  basalMetabolicRate: Power;
}

interface BloodGlucoseRecord extends InstantaneousRecord {
  recordType: 'bloodGlucose';
  level: BloodGlucose;
  specimenSource: number;
  mealType: number;
  relationToMeal: number;
}

export type RecordType = HealthConnectRecord['recordType'];

export interface Permission {
  accessType: 'read' | 'write';
  recordType: RecordType;
}

export interface ReadRecordsOptions {
  startTime: string;
  endTime: string;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
}

export type HealthConnectRecord =
  | ActiveCaloriesBurnedRecord
  | BasalBodyTemperatureRecord
  | BasalMetabolicRateRecord
  | BloodGlucoseRecord;

interface ActiveCaloriesBurnedRecordResult
  extends Omit<ActiveCaloriesBurnedRecord, 'energy'> {
  energy: {
    inCalories: number;
    inJoules: number;
    inKilocalories: number;
    inKilojoules: number;
  };
}

type HealthConnectRecordResult = ActiveCaloriesBurnedRecordResult;

export type RecordResult<T extends RecordType> = Extract<
  HealthConnectRecordResult,
  { recordType: T }
>;
