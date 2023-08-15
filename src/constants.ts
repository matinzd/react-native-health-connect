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

export const ExerciseSegmentType = {
  /** Use this type if the type of the exercise segment is not known. */
  UNKNOWN: 0,

  /** Use this type for arm curls. */
  ARM_CURL: 1,

  /** Use this type for back extensions. */
  BACK_EXTENSION: 2,

  /** Use this type for ball slams. */
  BALL_SLAM: 3,

  /** Use this type for barbel shoulder press. */
  BARBELL_SHOULDER_PRESS: 4,

  /** Use this type for bench presses. */
  BENCH_PRESS: 5,

  /** Use this type for bench sit up. */
  BENCH_SIT_UP: 6,

  /** Use this type for biking. */
  BIKING: 7,

  /** Use this type for stationary biking. */
  BIKING_STATIONARY: 8,

  /** Use this type for burpees. */
  BURPEE: 9,

  /** Use this type for crunches. */
  CRUNCH: 10,

  /** Use this type for deadlifts. */
  DEADLIFT: 11,

  /** Use this type for double arms triceps extensions. */
  DOUBLE_ARM_TRICEPS_EXTENSION: 12,

  /** Use this type for left arm dumbbell curl. */
  DUMBBELL_CURL_LEFT_ARM: 13,

  DUMBBELL_CURL_RIGHT_ARM: 14,

  /** Use this type for right arm dumbbell curl. */
  DUMBBELL_FRONT_RAISE: 15,

  /** Use this type for dumbbell lateral raises. */
  DUMBBELL_LATERAL_RAISE: 16,

  /** Use this type for dumbbells rows. */
  DUMBBELL_ROW: 17,

  /** Use this type for left arm triceps extensions. */
  DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM: 18,

  /** Use this type for right arm triceps extensions. */
  DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM: 19,

  /** Use this type for two arms triceps extensions. */
  DUMBBELL_TRICEPS_EXTENSION_TWO_ARM: 20,

  /** Use this type for elliptical workout. */
  ELLIPTICAL: 21,

  /** Use this type for forward twists. */
  FORWARD_TWIST: 22,

  /** Use this type for front raises. */
  FRONT_RAISE: 23,

  /** Use this type for high intensity training. */
  HIGH_INTENSITY_INTERVAL_TRAINING: 24,

  /** Use this type for hip thrusts. */
  HIP_THRUST: 25,

  /** Use this type for hula-hoops. */
  HULA_HOOP: 26,

  /** Use this type for jumping jacks. */
  JUMPING_JACK: 27,

  /** Use this type for jump rope. */
  JUMP_ROPE: 28,

  /** Use this type for kettlebell swings. */
  KETTLEBELL_SWING: 29,

  /** Use this type for lateral raises. */
  LATERAL_RAISE: 30,

  /** Use this type for lat pull-downs. */
  LAT_PULL_DOWN: 31,

  /** Use this type for leg curls. */
  LEG_CURL: 32,

  /** Use this type for leg extensions. */
  LEG_EXTENSION: 33,

  /** Use this type for leg presses. */
  LEG_PRESS: 34,

  /** Use this type for leg raises. */
  LEG_RAISE: 35,

  /** Use this type for lunges. */
  LUNGE: 36,

  /** Use this type for mountain climber. */
  MOUNTAIN_CLIMBER: 37,

  /** Use this type for other workout. */
  OTHER_WORKOUT: 38,

  /** Use this type for the pause. */
  PAUSE: 39,

  /** Use this type for pilates. */
  PILATES: 40,

  /** Use this type for plank. */
  PLANK: 41,

  /** Use this type for pull-ups. */
  PULL_UP: 42,

  /** Use this type for punches. */
  PUNCH: 43,

  /** Use this type for the rest. */
  REST: 44,

  /** Use this type for rowing machine workout. */
  ROWING_MACHINE: 45,

  /** Use this type for running. */
  RUNNING: 46,

  /** Use this type for treadmill running. */
  RUNNING_TREADMILL: 47,

  SHOULDER_PRESS: 48,

  /** Use this type for shoulder press. */
  SINGLE_ARM_TRICEPS_EXTENSION: 49,

  /** Use this type for sit-ups. */
  SIT_UP: 50,

  /** Use this type for squats. */
  SQUAT: 51,

  /** Use this type for stair climbing. */
  STAIR_CLIMBING: 52,

  /** Use this type for stair climbing machine. */
  STAIR_CLIMBING_MACHINE: 53,

  /** Use this type for stretching. */
  STRETCHING: 54,

  /** Use this type for backstroke swimming. */
  SWIMMING_BACKSTROKE: 55,

  /** Use this type for breaststroke swimming. */
  SWIMMING_BREASTSTROKE: 56,

  /** Use this type for butterfly swimming. */
  SWIMMING_BUTTERFLY: 57,

  SWIMMING_FREESTYLE: 58,

  /** Use this type for mixed swimming. */
  SWIMMING_MIXED: 59,

  /** Use this type for swimming in open water. */
  SWIMMING_OPEN_WATER: 60,

  /** Use this type if other swimming styles are not suitable. */
  SWIMMING_OTHER: 61,

  /** Use this type for swimming in the pool. */
  SWIMMING_POOL: 62,

  /** Use this type for upper twists. */
  UPPER_TWIST: 63,

  /** Use this type for walking. */
  WALKING: 64,

  /** Use this type for weightlifting. */
  WEIGHTLIFTING: 65,

  /** Use this type for wheelchair. */
  WHEELCHAIR: 66,

  /** Use this type for yoga. */
  YOGA: 67,
};

export const RecordingMethod = {
  /** For actively recorded data by the user. */
  RECORDING_METHOD_ACTIVELY_RECORDED: 1,

  /** For passively recorded data by the app. */
  RECORDING_METHOD_AUTOMATICALLY_RECORDED: 2,

  /** For manually entered data by the user. */
  RECORDING_METHOD_MANUAL_ENTRY: 3,

  /** Unknown recording method. */
  RECORDING_METHOD_UNKNOWN: 0,
};
