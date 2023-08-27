package com.example.foodcycle

import androidx.lifecycle.ViewModel
import com.example.foodcycle.data.Ingredient
import java.util.concurrent.CopyOnWriteArrayList

class MainViewModel: ViewModel() {

    // Search meals from database config
    var mealResult = ""
    fun saveMealResult(meals: String){
        mealResult = meals
    }

    // Search meals by ingredients
    var ingredientSearchResult = ""
    fun saveIngredientResult(res: String){
        ingredientSearchResult = res
    }

    // All mealMap in Search meals by ingredients
    var allMealMap =  CopyOnWriteArrayList<Map<String, String>>()
    fun saveAllMealMap(mapData: CopyOnWriteArrayList<Map<String, String>>){
        allMealMap = mapData
    }

    // All ingredientsList in Search meals by ingredients

    var allIngredientsData =  CopyOnWriteArrayList<List<Ingredient>>()
    fun saveAllIngredients(ingData: CopyOnWriteArrayList<List<Ingredient>>){
        allIngredientsData = ingData
    }


    // Configuration for Web Search

    var webSearchedData = ""
    fun saveWebSearchedData(result: String){
        webSearchedData = result
    }
}