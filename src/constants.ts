export const MeasurementLocation = {
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
} satisfies Record<string, number>;

export const MealType = {
  BREAKFAST: 1,
  DINNER: 3,
  LUNCH: 2,
  SNACK: 4,
  UNKNOWN: 0,
} satisfies Record<string, number>;

export const RelationToMeal = {
  AFTER_MEAL: 4,
  BEFORE_MEAL: 3,
  FASTING: 2,
  GENERAL: 1,
  UNKNOWN: 0,
} satisfies Record<string, number>;

export const SpecimenSource = {
  CAPILLARY_BLOOD: 2,
  INTERSTITIAL_FLUID: 1,
  PLASMA: 3,
  SERUM: 4,
  TEARS: 5,
  UNKNOWN: 0,
  WHOLE_BLOOD: 6,
} satisfies Record<string, number>;
