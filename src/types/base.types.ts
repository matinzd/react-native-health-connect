export interface Energy {
  value: number;
  unit: 'calories' | 'joules' | 'kilocalories' | 'kilojoules';
}

export interface BloodGlucose {
  value: number;
  unit: 'milligramsPerDeciliter' | 'millimolesPerLiter';
}

export interface Power {
  value: number;
  unit: 'watts' | 'kilocaloriesPerDay';
}

export interface Temperature {
  value: number;
  unit: 'celsius' | 'fahrenheit';
}

export interface Pressure {
  value: number;
  unit: 'millimetersOfMercury';
}

export interface BaseRecord {
  metadata?: Metadata;
}

export interface InstantaneousRecord extends BaseRecord {
  time: string;
  zoneOffset?: string;
}

export interface IntervalRecord extends BaseRecord {
  startTime: string;
  startZoneOffset?: string;
  endTime: string;
  endZoneOffset?: string;
}

export interface Metadata {
  id: string;
  // package name of the app that created the record
  dataOrigin: string;
  lastModifiedTime: string;
  clientRecordId?: string;
  clientRecordVersion: number;
  // see: https://developer.android.com/reference/kotlin/androidx/health/connect/client/records/metadata/Device
  device: number;
}

export type TimeRangeFilter =
  | {
      operator: 'between';
      startTime: string;
      endTime: string;
    }
  | {
      operator: 'after';
      startTime: string;
    }
  | {
      operator: 'before';
      endTime: string;
    };
