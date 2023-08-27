package com.example.foodcycle.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealViewModel(application: Application): AndroidViewModel(application){

    val  readAllData : LiveData<List<Meal>>

    private val repository: MealRepository


    init{
        val mealDao = MealDatabase.getDatabase(application).mealDao()
        repository = MealRepository(mealDao)
        readAllData = repository.readAllMeals
    }

    fun addMeal (meal: Meal){
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.addMeal(meal)
        }
    }

    fun getMealByName (mealName: String){
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.getMealByName(mealName)
        }
    }



}