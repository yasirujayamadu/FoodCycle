package com.example.foodcycle.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend  fun addMeal(meal: Meal)

//    @Query("SELECT * FROM meals WHERE meal = :mealName")
//     suspend fun getMealByName(mealName: String): List<Meal>?
    @Query ("SELECT * FROM meals WHERE meal LIKE :mealName || '%'")
    suspend fun getMealByName(mealName: String): List<Meal>?
    @Query("SELECT * FROM meals WHERE ingredients = :ingredientName")
     fun getMealByIngredient(ingredientName: String): List<Meal>

    @Query("SELECT * FROM meals")
    fun readAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM meals")
    fun readAllMealIng(): List<Meal>
}