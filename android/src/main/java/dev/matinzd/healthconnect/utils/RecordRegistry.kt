package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.records.*
import dev.matinzd.healthconnect.records.*
import kotlin.reflect.KClass

data class ReactRecordBinding(
  val reactType: String,
  val healthConnectClass: KClass<out Record>,
  val reactClass: Class<out ReactHealthRecordImpl<*>>,
)

val reactRecordBindings = listOf(
  ReactRecordBinding("ActiveCaloriesBurned", ActiveCaloriesBurnedRecord::class, ReactActiveCaloriesBurnedRecord::class.java),
  ReactRecordBinding("BasalBodyTemperature", BasalBodyTemperatureRecord::class, ReactBasalBodyTemperatureRecord::class.java),
  ReactRecordBinding("BasalMetabolicRate", BasalMetabolicRateRecord::class, ReactBasalMetabolicRateRecord::class.java),
  ReactRecordBinding("BloodGlucose", BloodGlucoseRecord::class, ReactBloodGlucoseRecord::class.java),
  ReactRecordBinding("BloodPressure", BloodPressureRecord::class, ReactBloodPressureRecord::class.java),
  ReactRecordBinding("BodyFat", BodyFatRecord::class, ReactBodyFatRecord::class.java),
  ReactRecordBinding("BodyTemperature", BodyTemperatureRecord::class, ReactBodyTemperatureRecord::class.java),
  ReactRecordBinding("BodyWaterMass", BodyWaterMassRecord::class, ReactBodyWaterMassRecord::class.java),
  ReactRecordBinding("BoneMass", BoneMassRecord::class, ReactBoneMassRecord::class.java),
  ReactRecordBinding("CervicalMucus", CervicalMucusRecord::class, ReactCervicalMucusRecord::class.java),
  ReactRecordBinding("CyclingPedalingCadence", CyclingPedalingCadenceRecord::class, ReactCyclingPedalingCadenceRecord::class.java),
  ReactRecordBinding("Distance", DistanceRecord::class, ReactDistanceRecord::class.java),
  ReactRecordBinding("ElevationGained", ElevationGainedRecord::class, ReactElevationGainedRecord::class.java),
  ReactRecordBinding("ExerciseSession", ExerciseSessionRecord::class, ReactExerciseSessionRecord::class.java),
  ReactRecordBinding("FloorsClimbed", FloorsClimbedRecord::class, ReactFloorsClimbedRecord::class.java),
  ReactRecordBinding("HeartRate", HeartRateRecord::class, ReactHeartRateRecord::class.java),
  ReactRecordBinding("HeartRateVariabilityRmssd", HeartRateVariabilityRmssdRecord::class, ReactHeartRateVariabilityRmssdRecord::class.java),
  ReactRecordBinding("Height", HeightRecord::class, ReactHeightRecord::class.java),
  ReactRecordBinding("Hydration", HydrationRecord::class, ReactHydrationRecord::class.java),
  ReactRecordBinding("LeanBodyMass", LeanBodyMassRecord::class, ReactLeanBodyMassRecord::class.java),
  ReactRecordBinding("MenstruationFlow", MenstruationFlowRecord::class, ReactMenstruationFlowRecord::class.java),
  ReactRecordBinding("Nutrition", NutritionRecord::class, ReactNutritionRecord::class.java),
  ReactRecordBinding("OvulationTest", OvulationTestRecord::class, ReactOvulationTestRecord::class.java),
  ReactRecordBinding("OxygenSaturation", OxygenSaturationRecord::class, ReactOxygenSaturationRecord::class.java),
  ReactRecordBinding("Power", PowerRecord::class, ReactPowerRecord::class.java),
  ReactRecordBinding("RespiratoryRate", RespiratoryRateRecord::class, ReactRespiratoryRateRecord::class.java),
  ReactRecordBinding("RestingHeartRate", RestingHeartRateRecord::class, ReactRestingHeartRateRecord::class.java),
  ReactRecordBinding("SexualActivity", SexualActivityRecord::class, ReactSexualActivityRecord::class.java),
  ReactRecordBinding("SleepSession", SleepSessionRecord::class, ReactSleepSessionRecord::class.java),
  ReactRecordBinding("Speed", SpeedRecord::class, ReactSpeedRecord::class.java),
  ReactRecordBinding("StepsCadence", StepsCadenceRecord::class, ReactStepsCadenceRecord::class.java),
  ReactRecordBinding("Steps", StepsRecord::class, ReactStepsRecord::class.java),
  ReactRecordBinding("TotalCaloriesBurned", TotalCaloriesBurnedRecord::class, ReactTotalCaloriesBurnedRecord::class.java),
  ReactRecordBinding("Vo2Max", Vo2MaxRecord::class, ReactVo2MaxRecord::class.java),
  ReactRecordBinding("Weight", WeightRecord::class, ReactWeightRecord::class.java),
  ReactRecordBinding("WheelchairPushes", WheelchairPushesRecord::class, ReactWheelchairPushesRecord::class.java),
  ReactRecordBinding("IntermenstrualBleeding", IntermenstrualBleedingRecord::class, ReactIntermenstrualBleedingRecord::class.java),
  ReactRecordBinding("MenstruationPeriod", MenstruationPeriodRecord::class, ReactMenstruationPeriodRecord::class.java),
)

val reactRecordTypeToClassMap: Map<String, KClass<out Record>> =
  reactRecordBindings.associate { it.reactType to it.healthConnectClass }

val reactRecordTypeToReactClassMap: Map<String, Class<out ReactHealthRecordImpl<*>>> =
  reactRecordBindings.associate { it.reactType to it.reactClass }

val reactClassToReactTypeMap: Map<Class<out ReactHealthRecordImpl<*>>, String> =
  reactRecordBindings.associate { it.reactClass to it.reactType }

val healthConnectClassToReactClassMap: Map<Class<out Record>, Class<out ReactHealthRecordImpl<*>>> =
  reactRecordBindings.associate { it.healthConnectClass.java to it.reactClass }
