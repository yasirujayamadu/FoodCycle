package com.example.foodcycle.fragments.searchMealByIngredient

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodcycle.MainViewModel
import com.example.foodcycle.data.Ingredient
import com.example.foodcycle.data.Meal
import com.example.foodcycle.data.MealViewModel
import com.example.foodcycle.databinding.FragmentSearchMealBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread



class SearchMealFragment : Fragment() {
    private lateinit var viewModel: MealViewModel
    private lateinit var binding: FragmentSearchMealBinding
    private lateinit var allMealMap: CopyOnWriteArrayList<Map<String, String>>
    private lateinit var allIngredient: CopyOnWriteArrayList<List<Ingredient>>
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchMealBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MealViewModel::class.java)
        allMealMap = CopyOnWriteArrayList()
        allIngredient = CopyOnWriteArrayList()

        binding.textView.movementMethod = ScrollingMovementMethod()

        // Configuration view model
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // retrieve data from view model back
        binding.textView.text = mainViewModel.ingredientSearchResult
        allMealMap = mainViewModel.allMealMap
        allIngredient = mainViewModel.allIngredientsData

        // Retrieve Btn
        binding.retrieveBtn.setOnClickListener {
            //binding.textView.text = mainViewModel.ingredientSearchResult

            if (binding.searchMealTextArea.text.isEmpty()) {
                Toast.makeText(context, "Enter the ingredient", Toast.LENGTH_SHORT).show()

            } else {
                runBlocking {
                    launch {

                        val list = getResponseFromWeb(binding.searchMealTextArea.text.toString())
                        Log.d("returnedMealIDList:::", list.toString())
                        getDataUsingMealId(list)

                    }
                }

            }
        }

        // save to database btn

        binding.saveToDb.setOnClickListener {

            if (allIngredient.size != 0) {
                Toast.makeText(context, "${allMealMap.size} meals saved in the database.", Toast.LENGTH_SHORT).show()
                insertDataToDatabase()
                Log.d("All ingredients", allIngredient[0].toString())
            }


        }

        //return inflater.inflate(R.layout.fragment_search_meal, container, false)

        return binding.root
    }

    private fun insertDataToDatabase() {
        if (allMealMap.size == 0) {
            return
        }
        Log.d("Data:", allMealMap[0].toString())

        // get total all mealmap size
        var objCount = 0
        var ingredientCount = 0
//        var idCount = 5
       for (i in 0 until allMealMap.size) {
            var singleMap = allMealMap[objCount]


            var meal = Meal(
                null,
                singleMap["Meal"],
                singleMap["DrinkAlternate"],
                singleMap["Category"],
                singleMap["Area"],
                singleMap["Instructions"],
                singleMap["MealThumb"],
                singleMap["Tags"],
                singleMap["Youtube"],
                allIngredient[ingredientCount],
                singleMap["Source"],
                singleMap["ImageSource"],
                singleMap["CreativeCommonsConfirmed"],
                singleMap["DateModified"]
            )

            // add meal to Db (it will run depend on the search result)
            viewModel.addMeal(meal)

            //idCount++
            ingredientCount++
            objCount++

        }
    }

    private fun getDataUsingMealId(jsonArr: JSONArray) {
        allMealMap.clear()
        // get data using meal id
        thread {
            for (i in 0 until jsonArr.length()) {
                val urlString =
                    "https://www.themealdb.com/api/json/v1/1/lookup.php?i=${jsonArr.getString(i)}"
                //Log.d("URL", urlString)
                val stb = java.lang.StringBuilder()
                val url = URL(urlString)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                print("reader::$reader")
                var line: String? = reader.readLine()
                while (line != null) {
                    stb.append(line + "\n")
                    line = reader.readLine()
                }
                reader.close()

                val jsonObj = JSONObject(stb.toString())
                val jsonArray: JSONArray = jsonObj.getJSONArray("meals")

                // call changeDataFormat method here
                var eachFormatData = changeDataFormat(jsonArray)
                allMealMap.add(eachFormatData)

                binding.textView.text = convertToJson(allMealMap)

                mainViewModel.saveIngredientResult(binding.textView.text.toString())
                mainViewModel.saveAllMealMap(allMealMap)

                Log.d("ScrollList::", convertToJson(allMealMap))
            }

        }




    }

    private fun changeDataFormat(jsonArr: JSONArray): Map<String, String> {
        val mealObject = jsonArr.getJSONObject(0)
        val mealMap = mutableMapOf<String, String>()

        mealMap["Meal"] = mealObject.getString("strMeal")
        mealMap["DrinkAlternate"] = mealObject.getString("strDrinkAlternate")
        mealMap["Category"] = mealObject.getString("strCategory")
        mealMap["Area"] = mealObject.getString("strArea")
        mealMap["Instructions"] = mealObject.getString("strInstructions")
        mealMap["MealThumb"] = mealObject.getString("strMealThumb")
        mealMap["Tags"] = mealObject.getString("strTags")
        mealMap["Youtube"] = mealObject.getString("strYoutube")

        val ingredientPattern = "^strIngredient\\d+".toRegex()
        val measurePattern = "^strMeasure\\d+".toRegex()

        val ingredients = mealObject.keys().asSequence().filter { ingredientPattern.matches(it) }
            .map { mealObject.getString(it) }.toList()
        val measurements = mealObject.keys().asSequence().filter { measurePattern.matches(it) }
            .map { mealObject.getString(it) }.toList()

        // format ingredients
        var ingredientCount = 1
        var objCount = 0 // to store in Ingredient
        val list = mutableListOf<Ingredient>()
        for (i in ingredients.indices) {

            list.add(
                Ingredient(name = ingredients[objCount], measurement = measurements[objCount])
            )

            allIngredient.add(list)
            // add to view model
            mainViewModel.saveAllIngredients(allIngredient)
            objCount++

            mealMap["Ingredient$ingredientCount"] = ingredients[i]
            ingredientCount++
        }


        // format measurements

        var measurementCount = 1
        for (i in measurements.indices) {


            mealMap["Measure$measurementCount"] = measurements[i]
            measurementCount++
        }

        mealMap["Source"] = mealObject.getString("strSource")
        mealMap["ImageSource"] = mealObject.getString("strImageSource")
        mealMap["CreativeCommonsConfirmed"] = mealObject.getString("strCreativeCommonsConfirmed")
        mealMap["DateModified"] = mealObject.getString("dateModified")



        return mealMap

    }

    @SuppressLint("SetTextI18n")
    private suspend fun getResponseFromWeb(text: String): JSONArray = withContext(Dispatchers.IO) {
        val mealIdArr = JSONArray()

        // get data using meal name
        val urlString = "https://www.themealdb.com/api/json/v1/1/filter.php?i=$text"
        val stb = StringBuilder()
        val url = URL(urlString)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

        val bf = BufferedReader(InputStreamReader(connection.inputStream))

        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }
        bf.close()

        Log.d("Stb :", stb.toString())

        val json = JSONObject(stb.toString())

        if (!json.isNull("meals")) {
            val jsonArray: JSONArray = json.getJSONArray("meals")
            for (i in 0 until jsonArray.length()) {
                val meal: JSONObject = jsonArray[i] as JSONObject // this is a json object
                val mealId = meal["idMeal"] as String

                mealIdArr.put(mealId)
            }
        }else{
            Log.d("Web respose :::", "No meal found")
            binding.textView.text = "\t\t\t\t\t\t\t\t\t\tNo meals found."
            //Toast.makeText(context, "No meal found", Toast.LENGTH_SHORT).show()
        }
        mealIdArr
    }


}


fun convertToJson(list: CopyOnWriteArrayList<Map<String, String>>): String {
    val result = StringBuilder()
    result.append("\n")

    list.forEachIndexed { index, map ->
        map.filter { (key, value) ->
            !value.isNullOrBlank() && key !in listOf(
                "MealThumb",
                "Source",
                "ImageSource",
                "CreativeCommonsConfirmed",
                "DateModified"
            )
        }
            .forEach { (key, value) ->
                result.append("\"$key\":")
                var newValue = value
                if (key == "Instructions") {
                    newValue = value.split(".").first()  // get only the first sentence
                }
                // Split value by comma and join them with ",\n"
                newValue = newValue.split(",").joinToString(",\n")
                result.append("\"$newValue\",\n")
            }
        // Remove the last comma and newline
        if (result.last() == ',' && result[result.length - 2] == '\n') {
            result.delete(result.length - 2, result.length)
        }
        // If it's not the last element, append a comma
        if (index != list.size - 1) {
            result.append(",\n")
        }
    }

    result.append("\n")
    return result.toString()
}



