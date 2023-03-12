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

export interface Energy {
  value: number;
  unit: 'calories' | 'joules' | 'kilocalories' | 'kilojoules';
}

export interface EnergyResult {
  inCalories: number;
  inJoules: number;
  inKilocalories: number;
  inKilojoules: number;
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

export interface PressureResult {
  inMillimetersOfMercury: number;
}

export interface Mass {
  value: number;
  unit:
    | 'grams'
    | 'kilograms'
    | 'milligrams'
    | 'micrograms'
    | 'ounces'
    | 'pounds';
}

export interface MassResult {
  inGrams: number;
  inKilograms: number;
  inMilligrams: number;
  inMicrograms: number;
  inOunces: number;
  inPounds: number;
}

export interface Length {
  value: number;
  unit: 'meters' | 'kilometers' | 'miles' | 'inches' | 'feet';
}

export interface LengthResult {
  inMeters: number;
  inKilometers: number;
  inMiles: number;
  inInches: number;
  inFeet: number;
}

export interface Volume {
  value: number;
  unit: 'liters' | 'fluidOuncesUs' | 'milliliters';
}

export interface VolumeResult {
  inLiters: number;
  inFluidOuncesUs: number;
  inMilliliters: number;
}

interface BaseSample {
  time: string;
}

export interface CyclingPedalingCadenceSample extends BaseSample {
  // Cycling revolutions per minute. Valid range: 0-10000.
  revolutionsPerMinute: number;
}

export interface HeartRateSample extends BaseSample {
  // Heart beats per minute. Validation range: 1-300.
  beatsPerMinute: number;
}
