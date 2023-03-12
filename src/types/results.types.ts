import type {
  EnergyResult,
  IntervalRecord,
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

interface NutritionRecordResult extends IntervalRecord {
  /** Biotin in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  biotin?: MassResult;
  /** Caffeine in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  caffeine?: MassResult;
  /** Calcium in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  calcium?: MassResult;
  /** Energy in [Energy] unit. Optional field. Valid range: 0-100000 kcal. */
  energy?: EnergyResult;
  /** Energy from fat in [Energy] unit. Optional field. Valid range: 0-100000 kcal. */
  energyFromFat?: EnergyResult;
  /** Chloride in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  chloride?: MassResult;
  /** Cholesterol in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  cholesterol?: MassResult;
  /** Chromium in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  chromium?: MassResult;
  /** Copper in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  copper?: MassResult;
  /** Dietary fiber in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  dietaryFiber?: MassResult;
  /** Folate in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  folate?: MassResult;
  /** Folic acid in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  folicAcid?: MassResult;
  /** Iodine in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  iodine?: MassResult;
  /** Iron in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  iron?: MassResult;
  /** Magnesium in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  magnesium?: MassResult;
  /** Manganese in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  manganese?: MassResult;
  /** Molybdenum in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  molybdenum?: MassResult;
  /** Monounsaturated fat in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  monounsaturatedFat?: MassResult;
  /** Niacin in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  niacin?: MassResult;
  /** Pantothenic acid in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  pantothenicAcid?: MassResult;
  /** Phosphorus in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  phosphorus?: MassResult;
  /** Polyunsaturated fat in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  polyunsaturatedFat?: MassResult;
  /** Potassium in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  potassium?: MassResult;
  /** Protein in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  protein?: MassResult;
  /** Riboflavin in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  riboflavin?: MassResult;
  /** Saturated fat in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  saturatedFat?: MassResult;
  /** Selenium in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  selenium?: MassResult;
  /** Sodium in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  sodium?: MassResult;
  /** Sugar in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  sugar?: MassResult;
  /** Thiamin in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  thiamin?: MassResult;
  /** Total carbohydrate in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  totalCarbohydrate?: MassResult;
  /** Total fat in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  totalFat?: MassResult;
  /** Trans fat in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  transFat?: MassResult;
  /** Unsaturated fat in [MassResult] unit. Optional field. Valid range: 0-100000 grams. */
  unsaturatedFat?: MassResult;
  /** Vitamin A in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminA?: MassResult;
  /** Vitamin B12 in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminB12?: MassResult;
  /** Vitamin B6 in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminB6?: MassResult;
  /** Vitamin C in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminC?: MassResult;
  /** Vitamin D in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminD?: MassResult;
  /** Vitamin E in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminE?: MassResult;
  /** Vitamin K in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  vitaminK?: MassResult;
  /** Zinc in [MassResult] unit. Optional field. Valid range: 0-100 grams. */
  zinc?: MassResult;
  /** Name for food or drink, provided by the user. Optional field. */
  name?: String;
  /** Check MealType constant */
  mealType: number;
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
  | WeightRecordResult
  | NutritionRecordResult;

export type RecordResult<T extends RecordType> = Omit<
  Extract<HealthConnectRecordResult, { recordType: T }>,
  'recordType'
>;
