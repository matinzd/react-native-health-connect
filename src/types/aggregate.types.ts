import type {
  EnergyResult,
  LengthResult,
  MassResult,
  PowerResult,
  PressureResult,
  TimeRangeFilter,
  VelocityResult,
  VolumeResult,
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

interface FloorsClimbedAggregateResult extends BaseAggregate {
  recordType: 'FloorsClimbed';
  FLOORS_CLIMBED_TOTAL: number;
}

interface CyclingPedalingCadenceAggregateResult extends BaseAggregate {
  recordType: 'CyclingPedalingCadence';
  RPM_AVG: number;
  RPM_MAX: number;
  RPM_MIN: number;
}

interface HeartRateAggregateResult extends BaseAggregate {
  recordType: 'HeartRate';
  BPM_AVG: number;
  BPM_MAX: number;
  BPM_MIN: number;
  MEASUREMENTS_COUNT: number;
}

interface RestingHeartRateAggregateResult extends BaseAggregate {
  recordType: 'RestingHeartRate';
  BPM_AVG: number;
  BPM_MAX: number;
  BPM_MIN: number;
}

interface StepsAggregateResult extends BaseAggregate {
  recordType: 'Steps';
  COUNT_TOTAL: number;
}

interface DistanceAggregateResult extends BaseAggregate {
  recordType: 'Distance';
  DISTANCE: LengthResult;
}

interface HeightAggregateResult extends BaseAggregate {
  recordType: 'Height';
  HEIGHT_AVG: LengthResult;
  HEIGHT_MIN: LengthResult;
  HEIGHT_MAX: LengthResult;
}

interface HydrationAggregateResult extends BaseAggregate {
  recordType: 'Hydration';
  VOLUME_TOTAL: VolumeResult;
}

interface WeightAggregateResult extends BaseAggregate {
  recordType: 'Weight';
  WEIGHT_AVG: MassResult;
  WEIGHT_MAX: MassResult;
  WEIGHT_MIN: MassResult;
}

interface NutritionAggregateResult extends BaseAggregate {
  recordType: 'Nutrition';
  BIOTIN_TOTAL: MassResult;
  CAFFEINE_TOTAL: MassResult;
  CALCIUM_TOTAL: MassResult;
  ENERGY_TOTAL: EnergyResult;
  ENERGY_FROM_FAT_TOTAL: EnergyResult;
  CHLORIDE_TOTAL: MassResult;
  CHOLESTEROL_TOTAL: MassResult;
  CHROMIUM_TOTAL: MassResult;
  COPPER_TOTAL: MassResult;
  DIETARY_FIBER_TOTAL: MassResult;
  FOLATE_TOTAL: MassResult;
  FOLIC_ACID_TOTAL: MassResult;
  IODINE_TOTAL: MassResult;
  IRON_TOTAL: MassResult;
  MAGNESIUM_TOTAL: MassResult;
  MANGANESE_TOTAL: MassResult;
  MOLYBDENUM_TOTAL: MassResult;
  MONOUNSATURATED_FAT_TOTAL: MassResult;
  NIACIN_TOTAL: MassResult;
  PANTOTHENIC_ACID_TOTAL: MassResult;
  PHOSPHORUS_TOTAL: MassResult;
  POLYUNSATURATED_FAT_TOTAL: MassResult;
  POTASSIUM_TOTAL: MassResult;
  PROTEIN_TOTAL: MassResult;
  RIBOFLAVIN_TOTAL: MassResult;
  SATURATED_FAT_TOTAL: MassResult;
  SELENIUM_TOTAL: MassResult;
  SODIUM_TOTAL: MassResult;
  SUGAR_TOTAL: MassResult;
  THIAMIN_TOTAL: MassResult;
  TOTAL_CARBOHYDRATE_TOTAL: MassResult;
  TOTAL_FAT_TOTAL: MassResult;
  ZINC_TOTAL: MassResult;
  VITAMIN_A_TOTAL: MassResult;
  VITAMIN_B12_TOTAL: MassResult;
  VITAMIN_B6_TOTAL: MassResult;
  VITAMIN_C_TOTAL: MassResult;
  VITAMIN_D_TOTAL: MassResult;
  VITAMIN_E_TOTAL: MassResult;
  VITAMIN_K_TOTAL: MassResult;
}

interface SpeedAggregateResult extends BaseAggregate {
  recordType: 'Speed';
  SPEED_AVG: VelocityResult;
  SPEED_MAX: VelocityResult;
  SPEED_MIN: VelocityResult;
}

interface SleepSessionAggregateResult extends BaseAggregate {
  recordType: 'SleepSession';
  SLEEP_DURATION_TOTAL: number;
}

interface WheelchairPushesAggregateResult extends BaseAggregate {
  recordType: 'WheelchairPushes';
  COUNT_TOTAL: number;
}

interface StepsCadenceAggregateResult extends BaseAggregate {
  recordType: 'StepsCadence';
  RATE_AVG: number;
  RATE_MAX: number;
  RATE_MIN: number;
}

interface TotalCaloriesBurnedAggregateResult extends BaseAggregate {
  recordType: 'TotalCaloriesBurned';
  ENERGY_TOTAL: EnergyResult;
}

interface PowerAggregateResult extends BaseAggregate {
  recordType: 'Power';
  POWER_AVG: PowerResult;
  POWER_MIN: PowerResult;
  POWER_MAX: PowerResult;
}

interface ElevationGainedAggregateResult extends BaseAggregate {
  recordType: 'ElevationGained';
  ELEVATION_GAINED_TOTAL: LengthResult;
}

export type AggregateRecordResult =
  | ActiveCaloriesBurnedAggregateResult
  | BasalMetabolicRateAggregateResult
  | BloodPressureAggregateResult
  | ExerciseSessionAggregateResult
  | FloorsClimbedAggregateResult
  | CyclingPedalingCadenceAggregateResult
  | HeartRateAggregateResult
  | RestingHeartRateAggregateResult
  | StepsAggregateResult
  | DistanceAggregateResult
  | HeightAggregateResult
  | HydrationAggregateResult
  | WeightAggregateResult
  | NutritionAggregateResult
  | SpeedAggregateResult
  | SleepSessionAggregateResult
  | WheelchairPushesAggregateResult
  | StepsCadenceAggregateResult
  | TotalCaloriesBurnedAggregateResult
  | PowerAggregateResult
  | ElevationGainedAggregateResult;

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

export interface AggregateAndGroupRequest<T extends AggregateResultRecordType> {
  recordType: T;
  timeRangeFilter: TimeRangeFilter;
  // The interval in minutes to group the data by
  bucketInterval: number;
  dataOriginFilter?: string[];
}
