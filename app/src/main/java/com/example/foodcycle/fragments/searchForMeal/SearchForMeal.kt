package com.example.foodcycle.fragments.searchForMeal

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodcycle.MainViewModel
import com.example.foodcycle.data.Meal
import com.example.foodcycle.data.MealDatabase
import com.example.foodcycle.data.MealViewModel
import com.example.foodcycle.databinding.FragmentSearchForMealBinding
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


class SearchForMeal : Fragment() {
    private lateinit var binding: FragmentSearchForMealBinding
    private lateinit var viewModel: MealViewModel
    private lateinit var mealByIngredient: CopyOnWriteArrayList<Meal>
    private lateinit var mealByName: List<Meal>
    private lateinit var mealResult: TextView



    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        var mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]



        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        binding = FragmentSearchForMealBinding.inflate(inflater, container, false)
        val adapter = ListAdapter()
        val recyclerView = binding.recyclerview

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[MealViewModel::class.java]


        mealByName = listOf()
        mealByIngredient = CopyOnWriteArrayList()

        binding.mealRes.movementMethod = ScrollingMovementMethod()

        binding.mealRes.text = mainViewModel.mealResult



        binding.searchForBtn.setOnClickListener {
            binding.mealRes.text = mainViewModel.mealResult

            var userTypedText = binding.searchText.text.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

            Log.d("Typed text", userTypedText)


            if (userTypedText.isEmpty()) {
                Toast.makeText(context, "Type something", Toast.LENGTH_SHORT).show()
            }

            GlobalScope.launch {


                val itemDao =
                    context?.let { it1 -> MealDatabase.getDatabase(it1.applicationContext) }

                mealByName = itemDao?.mealDao()?.getMealByName(userTypedText)!!

                val mealByIngredient = itemDao.mealDao().readAllMealIng()


                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {


                        Log.d(
                            "Meals that contains $userTypedText : ",
                            getMealsContainingIngredient(
                                mealByIngredient,
                                userTypedText
                            ).size.toString()
                        )

                        var ingredientSearch =
                            getMealsContainingIngredient(mealByIngredient, userTypedText)

                        if (mealByName.isNotEmpty()) {

                            Toast.makeText(context, "${mealByName.size} meals received from the Database.", Toast.LENGTH_SHORT).show()

                            Log.d("Meal by name::", mealByName.toString())
                            Log.d("Meal by name::", formatMeals(mealByName))
                            // configuration added
                            binding.mealRes.text = formatMeals(mealByName)
                            mainViewModel.saveMealResult(binding.mealRes.text.toString())

                        } else if (ingredientSearch.isNotEmpty()) {
                            binding.mealRes.text = formatMeals(ingredientSearch)

                            mainViewModel.saveMealResult(binding.mealRes.text.toString())

                            Toast.makeText(context, "${ingredientSearch.size} meals received from the Database", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show()
                        }



                    }
                }

            }


        }
        return binding.root
    }


    private fun getMealsContainingIngredient(
        mealByIngredient: List<Meal>?,
        userTypedText: String
    ): List<Meal> {
        return mealByIngredient?.filter { meal ->
            meal.ingredients.any { ingredient ->
                // check the ingredient matches with the user input
                ingredient.name.contains(userTypedText, ignoreCase = true)
            }
        } ?: emptyList()
    }

    private fun formatMeals(meals: List<Meal>): String {
        val stringBuilder = StringBuilder()

        meals.forEach { meal ->
            stringBuilder.append("\"Meal\":\"${meal.meal}\",\n")
            stringBuilder.append("\"DrinkAlternate\":${meal.drinkAlternate?.let { "\"$it\"" } ?: "null"},\n")
            stringBuilder.append("\"Category\":\"${meal.category}\",\n")
            stringBuilder.append("\"Area\":\"${meal.area}\",\n")

            val instructions = meal.instructions?.split(Regex("(\\.\\s)|(\\.\\z)"))?.get(0)

            stringBuilder.append("\"Instructions\":\"${instructions}\",\n")
            stringBuilder.append("\"Tags\":\"${meal.tags}\",\n")
            stringBuilder.append("\"Youtube\":\"${meal.youtube}\",\n")

            meal.ingredients.forEachIndexed { index, ingredient ->
                if (ingredient.name.isNotBlank()) {
                    stringBuilder.append("\"Ingredient${index + 1}\":\"${ingredient.name}\",\n")
                }
            }
            meal.ingredients.forEachIndexed { index, ingredient ->
                if (ingredient.measurement.isNotBlank()) {
                    stringBuilder.append("\"Measure${index + 1}\":\"${ingredient.measurement}\",\n")
                }
            }


            if (stringBuilder.isNotEmpty()) {
                stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length)
            }

            stringBuilder.append("\n,\n")
        }


        if (stringBuilder.isNotEmpty()) {
            stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length)
        }

        return stringBuilder.toString()
    }







}





