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
  HeartRateSample,
  Volume,
  SpeedSample,
  StepsCadenceSample,
  PowerSample,
  ExerciseSegment,
  ExerciseLap,
  SleepStage,
  ExerciseRoute,
} from './base.types';

export interface ActiveCaloriesBurnedRecord extends IntervalRecord {
  recordType: 'ActiveCaloriesBurned';
  energy: Energy;
}

export interface BasalBodyTemperatureRecord extends InstantaneousRecord {
  recordType: 'BasalBodyTemperature';
  temperature: Temperature;
  // Use TemperatureMeasurementLocation constant
  measurementLocation?: number;
}

export interface BasalMetabolicRateRecord extends InstantaneousRecord {
  recordType: 'BasalMetabolicRate';
  basalMetabolicRate: Power;
}

export interface BloodGlucoseRecord extends InstantaneousRecord {
  recordType: 'BloodGlucose';
  level: BloodGlucose;
  // Use SpecimenSource constant
  specimenSource: number;
  // Use MealType constant
  mealType: number;
  // Use RelationToMeal constant
  relationToMeal: number;
}

export interface BloodPressureRecord extends InstantaneousRecord {
  recordType: 'BloodPressure';
  systolic: Pressure;
  diastolic: Pressure;
  // Use BloodPressureBodyPosition constant
  bodyPosition: number;
  // Use BloodPressureMeasurementLocation constant
  measurementLocation: number;
}

export interface BodyFatRecord extends InstantaneousRecord {
  recordType: 'BodyFat';
  percentage: number;
}

export interface BodyTemperatureRecord extends InstantaneousRecord {
  recordType: 'BodyTemperature';
  temperature: Temperature;
  // Use TemperatureMeasurementLocation constant
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
  // Use CervicalMucusAppearance constant
  appearance?: number;
  // Use CervicalMucusSensation constant
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
  // Use ExerciseType constant
  exerciseType: number;
  title?: string;
  notes?: string;
  laps?: ExerciseLap[];
  segments?: ExerciseSegment[];
  exerciseRoute?: ExerciseRoute;
}

export interface FloorsClimbedRecord extends IntervalRecord {
  recordType: 'FloorsClimbed';
  floors: number;
}

export interface HeartRateRecord extends IntervalRecord {
  recordType: 'HeartRate';
  samples: HeartRateSample[];
}

export interface RestingHeartRateRecord extends InstantaneousRecord {
  recordType: 'RestingHeartRate';
  beatsPerMinute: number;
}

export interface StepsRecord extends IntervalRecord {
  recordType: 'Steps';
  count: number;
}

export interface StepsCadenceRecord extends IntervalRecord {
  recordType: 'StepsCadence';
  samples: StepsCadenceSample[];
}

export interface DistanceRecord extends IntervalRecord {
  recordType: 'Distance';
  distance: Length;
}

export interface HeightRecord extends InstantaneousRecord {
  recordType: 'Height';
  height: Length;
}

export interface HydrationRecord extends IntervalRecord {
  recordType: 'Hydration';
  volume: Volume;
}

export interface HeartRateVariabilityRmssdRecord extends InstantaneousRecord {
  recordType: 'HeartRateVariabilityRmssd';
  heartRateVariabilityMillis: number;
}

export interface SexualActivityRecord extends InstantaneousRecord {
  recordType: 'SexualActivity';
  // Use ProtectionUsed constant
  protectionUsed: number;
}

export interface WeightRecord extends InstantaneousRecord {
  recordType: 'Weight';
  weight: Mass;
}

export interface NutritionRecord extends IntervalRecord {
  /** Biotin in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  biotin?: Mass;
  /** Caffeine in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  caffeine?: Mass;
  /** Calcium in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  calcium?: Mass;
  /** Energy in [Energy] unit. Optional field. Valid range: 0-100000 kcal. */
  energy?: Energy;
  /** Energy from fat in [Energy] unit. Optional field. Valid range: 0-100000 kcal. */
  energyFromFat?: Energy;
  /** Chloride in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  chloride?: Mass;
  /** Cholesterol in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  cholesterol?: Mass;
  /** Chromium in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  chromium?: Mass;
  /** Copper in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  copper?: Mass;
  /** Dietary fiber in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  dietaryFiber?: Mass;
  /** Folate in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  folate?: Mass;
  /** Folic acid in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  folicAcid?: Mass;
  /** Iodine in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  iodine?: Mass;
  /** Iron in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  iron?: Mass;
  /** Magnesium in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  magnesium?: Mass;
  /** Manganese in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  manganese?: Mass;
  /** Molybdenum in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  molybdenum?: Mass;
  /** Monounsaturated fat in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  monounsaturatedFat?: Mass;
  /** Niacin in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  niacin?: Mass;
  /** Pantothenic acid in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  pantothenicAcid?: Mass;
  /** Phosphorus in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  phosphorus?: Mass;
  /** Polyunsaturated fat in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  polyunsaturatedFat?: Mass;
  /** Potassium in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  potassium?: Mass;
  /** Protein in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  protein?: Mass;
  /** Riboflavin in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  riboflavin?: Mass;
  /** Saturated fat in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  saturatedFat?: Mass;
  /** Selenium in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  selenium?: Mass;
  /** Sodium in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  sodium?: Mass;
  /** Sugar in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  sugar?: Mass;
  /** Thiamin in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  thiamin?: Mass;
  /** Total carbohydrate in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  totalCarbohydrate?: Mass;
  /** Total fat in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  totalFat?: Mass;
  /** Trans fat in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  transFat?: Mass;
  /** Unsaturated fat in [Mass] unit. Optional field. Valid range: 0-100000 grams. */
  unsaturatedFat?: Mass;
  /** Vitamin A in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminA?: Mass;
  /** Vitamin B12 in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminB12?: Mass;
  /** Vitamin B6 in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminB6?: Mass;
  /** Vitamin C in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminC?: Mass;
  /** Vitamin D in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminD?: Mass;
  /** Vitamin E in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminE?: Mass;
  /** Vitamin K in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  vitaminK?: Mass;
  /** Zinc in [Mass] unit. Optional field. Valid range: 0-100 grams. */
  zinc?: Mass;
  /** Name for food or drink, provided by the user. Optional field. */
  name?: string;
  /** Check MealType constant */
  mealType: number;
}

export interface LeanBodyMassRecord extends InstantaneousRecord {
  recordType: 'LeanBodyMass';
  mass: Mass;
}

export interface IntermenstrualBleedingRecord extends InstantaneousRecord {
  recordType: 'IntermenstrualBleeding';
}

export interface SpeedRecord extends IntervalRecord {
  recordType: 'Speed';
  samples: SpeedSample[];
}

export interface MenstruationFlowRecord extends InstantaneousRecord {
  recordType: 'MenstruationFlow';
  // Use MenstruationFlow constant
  flow?: number;
}

export interface MenstruationPeriodRecord extends InstantaneousRecord {
  recordType: 'MenstruationPeriod';
}

export interface SleepSessionRecord extends IntervalRecord {
  recordType: 'SleepSession';
  stages?: SleepStage[];
  title?: string;
  notes?: string;
}

export interface RespiratoryRateRecord extends InstantaneousRecord {
  recordType: 'RespiratoryRate';
  rate: number;
}

export interface WheelchairPushesRecord extends IntervalRecord {
  recordType: 'WheelchairPushes';
  count: number;
}

export interface Vo2MaxRecord extends InstantaneousRecord {
  recordType: 'Vo2Max';
  vo2MillilitersPerMinuteKilogram: number;
  // Use Vo2MaxMeasurementMethod constant
  measurementMethod: number;
}

export interface OvulationTestRecord extends InstantaneousRecord {
  recordType: 'OvulationTest';
  // Use OvulationTestResult constant
  result: number;
}

export interface TotalCaloriesBurnedRecord extends IntervalRecord {
  recordType: 'TotalCaloriesBurned';
  energy: Energy;
}

export interface OxygenSaturationRecord extends InstantaneousRecord {
  recordType: 'OxygenSaturation';
  percentage: number;
}

export interface PowerRecord extends IntervalRecord {
  recordType: 'Power';
  samples: PowerSample[];
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
  | CyclingPedalingCadenceRecord
  | ElevationGainedRecord
  | ExerciseSessionRecord
  | FloorsClimbedRecord
  | HeartRateRecord
  | RestingHeartRateRecord
  | StepsRecord
  | StepsCadenceRecord
  | DistanceRecord
  | HeightRecord
  | HydrationRecord
  | HeartRateVariabilityRmssdRecord
  | SexualActivityRecord
  | WeightRecord
  | LeanBodyMassRecord
  | IntermenstrualBleedingRecord
  | SpeedRecord
  | MenstruationFlowRecord
  | MenstruationPeriodRecord
  | SleepSessionRecord
  | RespiratoryRateRecord
  | WheelchairPushesRecord
  | Vo2MaxRecord
  | OvulationTestRecord
  | TotalCaloriesBurnedRecord
  | OxygenSaturationRecord
  | PowerRecord;

export type RecordType = HealthConnectRecord['recordType'];

export interface ReadRecordsOptions {
  timeRangeFilter: TimeRangeFilter;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
}
