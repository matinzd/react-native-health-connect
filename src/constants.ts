export const SdkAvailabilityStatus = {
  SDK_UNAVAILABLE: 1,
  SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED: 2,
  SDK_AVAILABLE: 3,
} as const;

export const TemperatureMeasurementLocation = {
  ARMPIT: 1,
  EAR: 8,
  FINGER: 2,
  FOREHEAD: 3,
  MOUTH: 4,
  RECTUM: 5,
  TEMPORAL_ARTERY: 6,
  TOE: 7,
  UNKNOWN: 0,
  VAGINA: 10,
} as const;

export const MealType = {
  BREAKFAST: 1,
  DINNER: 3,
  LUNCH: 2,
  SNACK: 4,
  UNKNOWN: 0,
} as const;

export const RelationToMeal = {
  AFTER_MEAL: 4,
  BEFORE_MEAL: 3,
  FASTING: 2,
  GENERAL: 1,
  UNKNOWN: 0,
} as const;

export const SpecimenSource = {
  CAPILLARY_BLOOD: 2,
  INTERSTITIAL_FLUID: 1,
  PLASMA: 3,
  SERUM: 4,
  TEARS: 5,
  UNKNOWN: 0,
  WHOLE_BLOOD: 6,
} as const;

export const BloodPressureBodyPosition = {
  UNKNOWN: 0,
  STANDING_UP: 1,
  SITTING_DOWN: 2,
  LYING_DOWN: 3,
  RECLINING: 4,
} as const;

export const BloodPressureMeasurementLocation = {
  UNKNOWN: 0,
  LEFT_WRIST: 1,
  RIGHT_WRIST: 2,
  LEFT_UPPER_ARM: 3,
  RIGHT_UPPER_ARM: 4,
} as const;

export const CervicalMucusAppearance = {
  UNKNOWN: 0,
  DRY: 1,
  STICKY: 2,
  CREAMY: 3,
  WATERY: 4,
  EGG_WHITE: 5,
  APPEARANCE_UNUSUAL: 6,
} as const;

export const CervicalMucusSensation = {
  UNKNOWN: 0,
  LIGHT: 1,
  MEDIUM: 2,
  HEAVY: 3,
} as const;

export const ExerciseType = {
  OTHER_WORKOUT: 0,
  BACK_EXTENSION: 1,
  BADMINTON: 2,
  BARBELL_SHOULDER_PRESS: 3,
  BASEBALL: 4,
  BASKETBALL: 5,
  BENCH_PRESS: 6,
  BENCH_SIT_UP: 7,
  BIKING: 8,
  BIKING_STATIONARY: 9,
  BOOT_CAMP: 10,
  BOXING: 11,
  BURPEE: 12,
  CALISTHENICS: 13,
  CRICKET: 14,
  CRUNCH: 15,
  DANCING: 16,
  DEADLIFT: 17,
  DUMBBELL_CURL_LEFT_ARM: 18,
  DUMBBELL_CURL_RIGHT_ARM: 19,
  DUMBBELL_FRONT_RAISE: 20,
  DUMBBELL_LATERAL_RAISE: 21,
  DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM: 22,
  DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM: 23,
  DUMBBELL_TRICEPS_EXTENSION_TWO_ARM: 24,
  ELLIPTICAL: 25,
  EXERCISE_CLASS: 26,
  FENCING: 27,
  FOOTBALL_AMERICAN: 28,
  FOOTBALL_AUSTRALIAN: 29,
  FORWARD_TWIST: 30,
  FRISBEE_DISC: 31,
  GOLF: 32,
  GUIDED_BREATHING: 33,
  GYMNASTICS: 34,
  HANDBALL: 35,
  HIGH_INTENSITY_INTERVAL_TRAINING: 36,
  HIKING: 37,
  ICE_HOCKEY: 38,
  ICE_SKATING: 39,
  JUMPING_JACK: 40,
  JUMP_ROPE: 41,
  LAT_PULL_DOWN: 42,
  LUNGE: 43,
  MARTIAL_ARTS: 44,
  PADDLING: 46,
  PARAGLIDING: 47,
  PILATES: 48,
  PLANK: 49,
  RACQUETBALL: 50,
  ROCK_CLIMBING: 51,
  ROLLER_HOCKEY: 52,
  ROWING: 53,
  ROWING_MACHINE: 54,
  RUGBY: 55,
  RUNNING: 56,
  RUNNING_TREADMILL: 57,
  SAILING: 58,
  SCUBA_DIVING: 59,
  SKATING: 60,
  SKIING: 61,
  SNOWBOARDING: 62,
  SNOWSHOEING: 63,
  SOCCER: 64,
  SOFTBALL: 65,
  SQUASH: 66,
  SQUAT: 67,
  STAIR_CLIMBING: 68,
  STAIR_CLIMBING_MACHINE: 69,
  STRENGTH_TRAINING: 70,
  STRETCHING: 71,
  SURFING: 72,
  SWIMMING_OPEN_WATER: 73,
  SWIMMING_POOL: 74,
  TABLE_TENNIS: 75,
  TENNIS: 76,
  UPPER_TWIST: 77,
  VOLLEYBALL: 78,
  WALKING: 79,
  WATER_POLO: 80,
  WEIGHTLIFTING: 81,
  WHEELCHAIR: 82,
  YOGA: 83,
} as const;

export const ProtectionUsed = {
  UNKNOWN: 0,
  PROTECTED: 1,
  UNPROTECTED: 2,
} as const;

export const MenstruationFlow = {
  UNKNOWN: 0,
  LIGHT: 1,
  MEDIUM: 2,
  HEAVY: 3,
} as const;

export const SleepStageType = {
  UNKNOWN: 0,
  AWAKE: 1,
  SLEEPING: 2,
  OUT_OF_BED: 3,
  LIGHT: 4,
  DEEP: 5,
  REM: 6,
} as const;

export const Vo2MaxMeasurementMethod = {
  OTHER: 0,
  METABOLIC_CART: 1,
  HEART_RATE_RATIO: 2,
  COOPER_TEST: 3,
  MULTISTAGE_FITNESS_TEST: 4,
  ROCKPORT_FITNESS_TEST: 5,
} as const;

export const OvulationTestResult = {
  /**
   * Inconclusive result. Refers to ovulation test results that are indeterminate (e.g. may be
   * testing malfunction, user error, etc.). ". Any unknown value will also be returned as
   */
  INCONCLUSIVE: 0,

  /**
   * Positive fertility (may also be referred as "peak" fertility). Refers to the peak of the
   * luteinizing hormone (LH) surge and ovulation is expected to occur in 10-36 hours.
   */
  POSITIVE: 1,

  /**
   * High fertility. Refers to a rise in estrogen or luteinizing hormone that may signal the
   * fertile window (time in the menstrual cycle when conception is likely to occur).
   */
  HIGH: 2,

  /**
   * Negative fertility (may also be referred as "low" fertility). Refers to the time in the
   * cycle where fertility/conception is expected to be low.
   */
  NEGATIVE: 3,
} as const;
