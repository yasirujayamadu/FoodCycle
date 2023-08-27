package com.example.foodcycle.data

import androidx.lifecycle.LiveData


class MealRepository(private val mealDao: MealDao) {
    val readAllMeals : LiveData<List<Meal>> = mealDao.readAllMeals()

    suspend fun addMeal (meal: Meal)  {
        mealDao.addMeal(meal)
    }

    suspend fun getMealByName(mealName: String){
        mealDao.getMealByName(mealName)
    }

    suspend fun getMealByIngredient(ingredientName: String){
        mealDao.getMealByIngredient(ingredientName)
    }



}