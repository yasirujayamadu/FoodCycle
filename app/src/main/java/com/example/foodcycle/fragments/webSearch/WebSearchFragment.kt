package com.example.foodcycle.fragments.webSearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodcycle.MainViewModel
import com.example.foodcycle.databinding.FragmentWebSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class WebSearchFragment : Fragment() {
    private lateinit var binding: FragmentWebSearchBinding
    private lateinit var mainViewModel: MainViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        binding = FragmentWebSearchBinding.inflate(inflater, container, false)

        // Configuration view model
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.webMealRes.movementMethod = ScrollingMovementMethod()

        // load data from viewModel
        binding.webMealRes.text = mainViewModel.webSearchedData

        binding.webSearchForBtn.setOnClickListener {

            Log.d("Web btn::","Clicked")

            val mealName = binding.webSearchText.text.toString()

            if (mealName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val result = searchMeal(mealName)
                        withContext(Dispatchers.Main) {
                            binding.webMealRes.text = result
                            // Configuration add
                            mainViewModel.saveWebSearchedData(binding.webMealRes.text.toString())
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            binding.webMealRes.text = "Network error. Please check your internet connection and try again."
                            mainViewModel.saveWebSearchedData(binding.webMealRes.text.toString())
                        }
                    } catch (e: JSONException) {
                        withContext(Dispatchers.Main) {
                            binding.webMealRes.text = "\t\t\t\t\t\t\t\t\t\tNo results found."
                            mainViewModel.saveWebSearchedData(binding.webMealRes.text.toString())
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.webMealRes.text = "An unexpected error occurred. Please try again."
                            mainViewModel.saveWebSearchedData(binding.webMealRes.text.toString())
                        }
                    }
                }
            }
        }


        return binding.root
    }


    @Throws(IOException::class, JSONException::class)
    private suspend fun searchMeal(mealName: String): String {
        // Receive by meal name
        val url = URL("https://www.themealdb.com/api/json/v1/1/search.php?s=$mealName")
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            return parseJson(response)
        }
        throw IOException("HTTP error code: $responseCode")
    }

@Throws(JSONException::class)
private fun parseJson(json: String): String {
    val meals = StringBuilder()
    val jsonObject = JSONObject(json)
    val mealsArray = jsonObject.getJSONArray("meals")


    for (i in 0 until mealsArray.length()) {
        val mealObject = mealsArray.getJSONObject(i)

        meals.append("\"Meal\": \"").append(mealObject.getString("strMeal")).append("\",\n")
        meals.append("\"DrinkAlternate\": \"").append(mealObject.getString("strDrinkAlternate")).append("\",\n")
        meals.append("\"Category\": \"").append(mealObject.getString("strCategory")).append("\",\n")
        meals.append("\"Area\": \"").append(mealObject.getString("strArea")).append("\",\n")

        // limit Instructions into one sentence
        val fullInstructions = mealObject.getString("strInstructions")
        val firstSentence = fullInstructions.split(".")[0] + "."
        meals.append("\"Instructions\": \"").append(firstSentence).append("\",\n")

        meals.append("\"Tags\": \"").append(mealObject.getString("strTags")).append("\",\n")
        meals.append("\"Youtube\": \"").append(mealObject.getString("strYoutube")).append("\",\n")

        for (j in 1..20) {
            val ingredient = mealObject.optString("strIngredient$j", "")
            if (ingredient.isNotBlank()) {
                meals.append("\"Ingredient$j\": \"").append(ingredient).append("\",\n")
            }
        }
        for (j in 1..20) {
            val ingredient = mealObject.optString("strIngredient$j", "")
            val measure = mealObject.optString("strMeasure$j", "")
            if (ingredient.isNotBlank()) {
                meals.append("\"Measure$j\": \"").append(measure).append("\",\n")
            }
        }
        meals.append("-----------------------------------\n") // Add an extra line to divide the output

    }

    return meals.toString()
}

}