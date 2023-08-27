package com.example.foodcycle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Entity(tableName = "meals")
@TypeConverters(IngredientConverter::class)
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val idMeal: Int?,
    val meal: String?,
    val drinkAlternate: String?,
    val category: String?,
    val area: String?,
    val instructions: String?,
    val mealThumb: String?,
    val tags: String?,
    val youtube: String?,
    val ingredients: List<Ingredient>,
    val source: String?,
    val imageSource: String?,
    val CreativeCommonsConfirmed: String?,
    val dateModified: String?,

//    val measurements: List<Measure>
)

class IngredientConverter {
    @TypeConverter
    fun listToJson (value:  List<Ingredient>? ) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String ) = Gson().fromJson(value, Array<Ingredient>::class.java ).toList()
}


//class MeasurementConverter {
//    @TypeConverter
//    fun listToJson (value:  List<Measure>? ) = Gson().toJson(value)
//
//    @TypeConverter
//    fun jsonToList(value: String ) = Gson().fromJson(value, Array<Measure>::class.java ).toList()
//}



data class Ingredient(
    val name: String,
    val measurement: String
)

//data class Measure(
//    val name: String,
//    val measurement: String
//)