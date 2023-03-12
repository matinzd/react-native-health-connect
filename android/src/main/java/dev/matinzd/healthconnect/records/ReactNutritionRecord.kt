package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.MealType
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant

class ReactNutritionRecord : ReactHealthRecordImpl<NutritionRecord> {
  override fun parseWriteRecord(records: ReadableArray): List<NutritionRecord> {
    return records.toMapList().map { map ->
      NutritionRecord(
        startTime = Instant.parse(map.getString("startTime")),
        endTime = Instant.parse(map.getString("endTime")),
        startZoneOffset = null,
        endZoneOffset = null,
        biotin = getMassFromJsMap(map.getMap("biotin")),
        caffeine = getMassFromJsMap(map.getMap("caffeine")),
        calcium = getMassFromJsMap(map.getMap("calcium")),
        energy = getEnergyFromJsMap(map.getMap("energy")),
        energyFromFat = getEnergyFromJsMap(map.getMap("energyFromFat")),
        chloride = getMassFromJsMap(map.getMap("chloride")),
        cholesterol = getMassFromJsMap(map.getMap("cholesterol")),
        chromium = getMassFromJsMap(map.getMap("chromium")),
        copper = getMassFromJsMap(map.getMap("copper")),
        dietaryFiber = getMassFromJsMap(map.getMap("dietaryFiber")),
        folate = getMassFromJsMap(map.getMap("folate")),
        folicAcid = getMassFromJsMap(map.getMap("folicAcid")),
        iodine = getMassFromJsMap(map.getMap("iodine")),
        iron = getMassFromJsMap(map.getMap("iron")),
        magnesium = getMassFromJsMap(map.getMap("magnesium")),
        manganese = getMassFromJsMap(map.getMap("manganese")),
        molybdenum = getMassFromJsMap(map.getMap("molybdenum")),
        monounsaturatedFat = getMassFromJsMap(map.getMap("monounsaturatedFat")),
        niacin = getMassFromJsMap(map.getMap("niacin")),
        pantothenicAcid = getMassFromJsMap(map.getMap("pantothenicAcid")),
        phosphorus = getMassFromJsMap(map.getMap("phosphorus")),
        polyunsaturatedFat = getMassFromJsMap(map.getMap("polyunsaturatedFat")),
        potassium = getMassFromJsMap(map.getMap("potassium")),
        protein = getMassFromJsMap(map.getMap("protein")),
        riboflavin = getMassFromJsMap(map.getMap("riboflavin")),
        saturatedFat = getMassFromJsMap(map.getMap("saturatedFat")),
        selenium = getMassFromJsMap(map.getMap("selenium")),
        sodium = getMassFromJsMap(map.getMap("sodium")),
        sugar = getMassFromJsMap(map.getMap("sugar")),
        thiamin = getMassFromJsMap(map.getMap("thiamin")),
        totalCarbohydrate = getMassFromJsMap(map.getMap("totalCarbohydrate")),
        totalFat = getMassFromJsMap(map.getMap("totalFat")),
        transFat = getMassFromJsMap(map.getMap("transFat")),
        unsaturatedFat = getMassFromJsMap(map.getMap("unsaturatedFat")),
        vitaminA = getMassFromJsMap(map.getMap("vitaminA")),
        vitaminB12 = getMassFromJsMap(map.getMap("vitaminB12")),
        vitaminB6 = getMassFromJsMap(map.getMap("vitaminB6")),
        vitaminC = getMassFromJsMap(map.getMap("vitaminC")),
        vitaminD = getMassFromJsMap(map.getMap("vitaminD")),
        vitaminE = getMassFromJsMap(map.getMap("vitaminE")),
        vitaminK = getMassFromJsMap(map.getMap("vitaminK")),
        zinc = getMassFromJsMap(map.getMap("zinc")),
        name = map.getString("name"),
        mealType = map.getSafeInt("mealType", MealType.MEAL_TYPE_UNKNOWN),
      )
    }
  }

  override fun parseRecord(record: NutritionRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putMap("biotin", massToJsMap(record.biotin))
      putMap("caffeine", massToJsMap(record.caffeine))
      putMap("calcium", massToJsMap(record.calcium))
      putMap("energy", energyToJsMap(record.energy))
      putMap("energyFromFat", energyToJsMap(record.energyFromFat))
      putMap("chloride", massToJsMap(record.chloride))
      putMap("cholesterol", massToJsMap(record.cholesterol))
      putMap("chromium", massToJsMap(record.chromium))
      putMap("copper", massToJsMap(record.copper))
      putMap("dietaryFiber", massToJsMap(record.dietaryFiber))
      putMap("folate", massToJsMap(record.folate))
      putMap("folicAcid", massToJsMap(record.folicAcid))
      putMap("iodine", massToJsMap(record.iodine))
      putMap("iron", massToJsMap(record.iron))
      putMap("magnesium", massToJsMap(record.magnesium))
      putMap("manganese", massToJsMap(record.manganese))
      putMap("molybdenum", massToJsMap(record.molybdenum))
      putMap("monounsaturatedFat", massToJsMap(record.monounsaturatedFat))
      putMap("niacin", massToJsMap(record.niacin))
      putMap("pantothenicAcid", massToJsMap(record.pantothenicAcid))
      putMap("phosphorus", massToJsMap(record.phosphorus))
      putMap("polyunsaturatedFat", massToJsMap(record.polyunsaturatedFat))
      putMap("potassium", massToJsMap(record.potassium))
      putMap("protein", massToJsMap(record.protein))
      putMap("riboflavin", massToJsMap(record.riboflavin))
      putMap("saturatedFat", massToJsMap(record.saturatedFat))
      putMap("selenium", massToJsMap(record.selenium))
      putMap("sodium", massToJsMap(record.sodium))
      putMap("sugar", massToJsMap(record.sugar))
      putMap("thiamin", massToJsMap(record.thiamin))
      putMap("totalCarbohydrate", massToJsMap(record.totalCarbohydrate))
      putMap("totalFat", massToJsMap(record.totalFat))
      putMap("transFat", massToJsMap(record.transFat))
      putMap("unsaturatedFat", massToJsMap(record.unsaturatedFat))
      putMap("vitaminA", massToJsMap(record.vitaminA))
      putMap("vitaminB12", massToJsMap(record.vitaminB12))
      putMap("vitaminB6", massToJsMap(record.vitaminB6))
      putMap("vitaminC", massToJsMap(record.vitaminC))
      putMap("vitaminD", massToJsMap(record.vitaminD))
      putMap("vitaminE", massToJsMap(record.vitaminE))
      putMap("vitaminK", massToJsMap(record.vitaminK))
      putMap("zinc", massToJsMap(record.zinc))
      putString("name", record.name)
      putInt("mealType", record.mealType)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        NutritionRecord.BIOTIN_TOTAL,
        NutritionRecord.CAFFEINE_TOTAL,
        NutritionRecord.CALCIUM_TOTAL,
        NutritionRecord.ENERGY_TOTAL,
        NutritionRecord.ENERGY_FROM_FAT_TOTAL,
        NutritionRecord.CHLORIDE_TOTAL,
        NutritionRecord.CHOLESTEROL_TOTAL,
        NutritionRecord.CHROMIUM_TOTAL,
        NutritionRecord.COPPER_TOTAL,
        NutritionRecord.DIETARY_FIBER_TOTAL,
        NutritionRecord.FOLATE_TOTAL,
        NutritionRecord.FOLIC_ACID_TOTAL,
        NutritionRecord.IODINE_TOTAL,
        NutritionRecord.IRON_TOTAL,
        NutritionRecord.MAGNESIUM_TOTAL,
        NutritionRecord.MANGANESE_TOTAL,
        NutritionRecord.MOLYBDENUM_TOTAL,
        NutritionRecord.MONOUNSATURATED_FAT_TOTAL,
        NutritionRecord.NIACIN_TOTAL,
        NutritionRecord.PANTOTHENIC_ACID_TOTAL,
        NutritionRecord.PHOSPHORUS_TOTAL,
        NutritionRecord.POLYUNSATURATED_FAT_TOTAL,
        NutritionRecord.POTASSIUM_TOTAL,
        NutritionRecord.PROTEIN_TOTAL,
        NutritionRecord.RIBOFLAVIN_TOTAL,
        NutritionRecord.SATURATED_FAT_TOTAL,
        NutritionRecord.SELENIUM_TOTAL,
        NutritionRecord.SODIUM_TOTAL,
        NutritionRecord.SUGAR_TOTAL,
        NutritionRecord.THIAMIN_TOTAL,
        NutritionRecord.TOTAL_CARBOHYDRATE_TOTAL,
        NutritionRecord.TOTAL_FAT_TOTAL,
        NutritionRecord.ZINC_TOTAL,
        NutritionRecord.VITAMIN_A_TOTAL,
        NutritionRecord.VITAMIN_B12_TOTAL,
        NutritionRecord.VITAMIN_B6_TOTAL,
        NutritionRecord.VITAMIN_C_TOTAL,
        NutritionRecord.VITAMIN_D_TOTAL,
        NutritionRecord.VITAMIN_E_TOTAL,
        NutritionRecord.VITAMIN_K_TOTAL,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putMap("BIOTIN_TOTAL", massToJsMap(record[NutritionRecord.BIOTIN_TOTAL]))
      putMap("CAFFEINE_TOTAL", massToJsMap(record[NutritionRecord.CAFFEINE_TOTAL]))
      putMap("CALCIUM_TOTAL", massToJsMap(record[NutritionRecord.CALCIUM_TOTAL]))
      putMap("ENERGY_TOTAL", energyToJsMap(record[NutritionRecord.ENERGY_TOTAL]))
      putMap(
        "ENERGY_FROM_FAT_TOTAL", energyToJsMap(record[NutritionRecord.ENERGY_FROM_FAT_TOTAL])
      )
      putMap("CHLORIDE_TOTAL", massToJsMap(record[NutritionRecord.CHLORIDE_TOTAL]))
      putMap("CHOLESTEROL_TOTAL", massToJsMap(record[NutritionRecord.CHOLESTEROL_TOTAL]))
      putMap("CHROMIUM_TOTAL", massToJsMap(record[NutritionRecord.CHROMIUM_TOTAL]))
      putMap("COPPER_TOTAL", massToJsMap(record[NutritionRecord.COPPER_TOTAL]))
      putMap("DIETARY_FIBER_TOTAL", massToJsMap(record[NutritionRecord.DIETARY_FIBER_TOTAL]))
      putMap("FOLATE_TOTAL", massToJsMap(record[NutritionRecord.FOLATE_TOTAL]))
      putMap("FOLIC_ACID_TOTAL", massToJsMap(record[NutritionRecord.FOLIC_ACID_TOTAL]))
      putMap("IODINE_TOTAL", massToJsMap(record[NutritionRecord.IODINE_TOTAL]))
      putMap("IRON_TOTAL", massToJsMap(record[NutritionRecord.IRON_TOTAL]))
      putMap("MAGNESIUM_TOTAL", massToJsMap(record[NutritionRecord.MAGNESIUM_TOTAL]))
      putMap("MANGANESE_TOTAL", massToJsMap(record[NutritionRecord.MANGANESE_TOTAL]))
      putMap("MOLYBDENUM_TOTAL", massToJsMap(record[NutritionRecord.MOLYBDENUM_TOTAL]))
      putMap(
        "MONOUNSATURATED_FAT_TOTAL", massToJsMap(record[NutritionRecord.MONOUNSATURATED_FAT_TOTAL])
      )
      putMap("NIACIN_TOTAL", massToJsMap(record[NutritionRecord.NIACIN_TOTAL]))
      putMap("PANTOTHENIC_ACID_TOTAL", massToJsMap(record[NutritionRecord.PANTOTHENIC_ACID_TOTAL]))
      putMap("PHOSPHORUS_TOTAL", massToJsMap(record[NutritionRecord.PHOSPHORUS_TOTAL]))
      putMap(
        "POLYUNSATURATED_FAT_TOTAL", massToJsMap(record[NutritionRecord.POLYUNSATURATED_FAT_TOTAL])
      )
      putMap("POTASSIUM_TOTAL", massToJsMap(record[NutritionRecord.POTASSIUM_TOTAL]))
      putMap("PROTEIN_TOTAL", massToJsMap(record[NutritionRecord.PROTEIN_TOTAL]))
      putMap("RIBOFLAVIN_TOTAL", massToJsMap(record[NutritionRecord.RIBOFLAVIN_TOTAL]))
      putMap("SATURATED_FAT_TOTAL", massToJsMap(record[NutritionRecord.SATURATED_FAT_TOTAL]))
      putMap("SELENIUM_TOTAL", massToJsMap(record[NutritionRecord.SELENIUM_TOTAL]))
      putMap("SODIUM_TOTAL", massToJsMap(record[NutritionRecord.SODIUM_TOTAL]))
      putMap("SUGAR_TOTAL", massToJsMap(record[NutritionRecord.SUGAR_TOTAL]))
      putMap("THIAMIN_TOTAL", massToJsMap(record[NutritionRecord.THIAMIN_TOTAL]))
      putMap(
        "TOTAL_CARBOHYDRATE_TOTAL", massToJsMap(record[NutritionRecord.TOTAL_CARBOHYDRATE_TOTAL])
      )
      putMap("TOTAL_FAT_TOTAL", massToJsMap(record[NutritionRecord.TOTAL_FAT_TOTAL]))
      putMap("ZINC_TOTAL", massToJsMap(record[NutritionRecord.ZINC_TOTAL]))
      putMap("VITAMIN_A_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_A_TOTAL]))
      putMap("VITAMIN_B12_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_B12_TOTAL]))
      putMap("VITAMIN_B6_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_B6_TOTAL]))
      putMap("VITAMIN_C_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_C_TOTAL]))
      putMap("VITAMIN_D_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_D_TOTAL]))
      putMap("VITAMIN_E_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_E_TOTAL]))
      putMap("VITAMIN_K_TOTAL", massToJsMap(record[NutritionRecord.VITAMIN_K_TOTAL]))
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }
}
