package com.example.foodcycle.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodcycle.R
import com.example.foodcycle.data.Ingredient
import com.example.foodcycle.data.Meal
import com.example.foodcycle.data.MealViewModel
import com.example.foodcycle.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MealViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(MealViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)




        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchMealFragment)

        }

        binding.searchForMealBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchForMeal)
        }

        binding.saveToDbBtn.setOnClickListener{
            insertDataToDatabase()
        }

        binding.webSearchBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_webSearchFragment)
        }




        return binding.root
    }

    private fun insertDataToDatabase(){

        val instructionList = allInstructions()
        val ingredientList = allIngredients()

        val meal1 = Meal(null,
            "Sweet and Sour Pork",null,
            "Pork","Chinese", instructionList[0],
            "https://www.themealdb.com/images/media/meals/1529442316.jpg",
            "Sweet","https://www.youtube.com/watch?v=mdaBIhgEAMo", ingredientList[0],
            null,"",null,null)

        val meal2 = Meal(null,
            "Chicken Marengo",null,
            "Chicken","French", instructionList[1],
            "https://www.themealdb.com/images/media/meals/qpxvuq1511798906.jpg",
            null,"https://www.youtube.com/watch?v=U33HYUr-0Fw", ingredientList[1],
            null,null,null,null)

        val meal3 = Meal(null,
            "Beef Banh Mi Bowls with Sriracha Mayo, Carrot & Pickled Cucumber",
            null,
            "Beef","Vietnamese", instructionList[2],
            "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg",
            null,null, ingredientList[2],
            null,null,null,null)

        val meal4 = Meal(null,
            "Leblebi Soup",
            null,
            "Vegetarian","Tunisian", instructionList[3],
            "https://www.themealdb.com/images/media/meals/x2fw9e1560460636.jpg",
            "Soup","https://www.youtube.com/watch?v=BgRifcCwinY", ingredientList[3],
            "http://allrecipes.co.uk/recipe/43419/leblebi--tunisian-chickpea-soup-.aspx",
            null,null,null)


        viewModel.addMeal(meal1)
        viewModel.addMeal(meal2)
        viewModel.addMeal(meal3)
        viewModel.addMeal(meal4)



        Toast.makeText(context,"4  meal records added to the DataBase.", Toast.LENGTH_SHORT).show()
    }

    private fun allInstructions(): List<String> {

        val instruction1 =
            "Preparation\\r\\n1. Crack the egg into a bowl. Separate the egg white and yolk.\\r\\n\\r\\nSweet and Sour Pork\\r\\n2. Slice the pork tenderloin into ips.\\r\\n\\r\\n3. " +
                    "Prepare the marinade using a pinch of salt, one teaspoon of starch, " +
                    "two teaspoons of light soy sauce, and an egg white.\\r\\n\\r\\n4. " +
                    "Marinade the pork ips for about 20 minutes.\\r\\n\\r\\n5. " +
                    "Put the remaining starch in a bowl. " +
                    "Add some water and vinegar to make a starchy sauce.\\r\\n\\r\\nSweet and Sour Pork\\r\\nCooking Inuctions\\r\\n1. " +
                    "Pour the cooking oil into a wok and heat to 190\\u00b0C (375\\u00b0F). Add the marinated pork ips and fry them until they turn brown. Remove the cooked pork from the wok and place on a plate.\\r\\n\\r\\n2. Leave some oil in the wok. " +
                    "Put the tomato sauce and white sugar into the wok, and heat until the oil and sauce are fully combined.\\r\\n\\r\\n3. " +
                    "Add some water to the wok and thoroughly heat the sweet and sour sauce before adding the pork ips to it.\\r\\n\\r\\n4. Pour in the starchy sauce. Stir-fry all the ingredients until the pork and sauce are thoroughly mixed together.\\r\\n\\r\\n5. Serve on a plate and add some coriander for decoration."


        val instruction2 =
            "Heat the oil in a large flameproof casserole dish and stir-fry the mushrooms until they start to soften. " +
                    "Add the chicken legs and cook briefly on each side to colour them a little." +
                    "\r\nPour in the passata, crumble in the stock cube and stir in the olives. " +
                    "Season with black pepper \u2013 you shouldn\u2019t need salt. Cover and simmer for 40 mins until the chicken is tender. " +
                    "Sprinkle with parsley and serve with pasta and a salad, or mash and green veg, if you like."

        val instruction3 =
            "Add'l ingredients: mayonnaise, siracha\r\n\r\n1\r\n\r\nPlace rice in a fine-mesh sieve and rinse until water runs clear. " +
                    "Add to a small pot with 1 cup water (2 cups for 4 servings) and a pinch of salt. " +
                    "Bring to a boil, then cover and reduce heat to low. Cook until rice is tender, 15 minutes. " +
                    "Keep covered off heat for at least 10 minutes or until ready to serve." +
                    "\r\n\r\n2\r\n\r\nMeanwhile, wash and dry all produce. Peel and finely chop garlic. " +
                    "Zest and quarter lime (for 4 servings, zest 1 lime and quarter both). " +
                    "Trim and halve cucumber lengthwise; thinly slice crosswise into half-moons. Halve, peel, and medium dice onion. Trim, peel, and grate carrot.\r\n\r\n3\r\n\r\nIn a medium bowl, combine cucumber, juice from half the lime, \u00bc tsp sugar (\u00bd tsp for 4 servings), and a pinch of salt. In a small bowl, combine mayonnaise, a pinch of garlic, a squeeze of lime juice, and as much sriracha as you\u2019d like. Season with salt and pepper.\r\n\r\n4\r\n\r\nHeat a drizzle of oil in a large pan over medium-high heat. Add onion and cook, stirring, until softened, 4-5 minutes. Add beef, remaining garlic, and 2 tsp sugar (4 tsp for 4 servings). Cook, breaking up meat into pieces, until beef is browned and cooked through, 4-5 minutes. Stir in soy sauce. Turn off heat; taste and season with salt and pepper.\r\n\r\n5\r\n\r\nFluff rice with a fork; stir in lime zest and 1 TBSP butter. Divide rice between bowls. Arrange beef, grated carrot, and pickled cucumber on top. Top with a squeeze of lime juice. Drizzle with sriracha mayo."

        val instruction4 =
            "Heat the oil in a large pot. Add the onion and cook until translucent.\r\nDrain the soaked chickpeas and add them to the pot together with the vegetable stock. " +
                    "Bring to the boil, then reduce the heat and cover. Simmer for 30 minutes." +
                    "\r\nIn the meantime toast the cumin in a small ungreased frying pan, then grind them in a mortar. " +
                    "Add the garlic and salt and pound to a fine paste.\r\nAdd the paste and the harissa to the soup and simmer until the chickpeas are tender, about 30 minutes.\r\nSeason to taste with salt, pepper and lemon juice and serve hot."

        return listOf(instruction1, instruction2, instruction3, instruction4)

    }

    private fun allIngredients(): List<List<Ingredient>> {

        // Add the ingredients to a list
        val ingredientsList1 = listOf(
            Ingredient(name = "Pork", measurement = "200g"),
            Ingredient(name = "Egg", measurement = "1"),
            Ingredient(name = "Water", measurement = "Dash"),
            Ingredient(name = "Salt", measurement = "1/2 tsp"),
            Ingredient(name = "Soy Sauce", measurement = "10g"),
            Ingredient(name = "Starch", measurement = "10g"),
            Ingredient(name = "Starch", measurement = "10g"),
            Ingredient(name = "Tomato Puree", measurement = "30g"),
            Ingredient(name = "Vinegar", measurement = "10g"),
            Ingredient(name = "Coriander", measurement = "Dash")
        )

        val ingredientsList2 = listOf(
            Ingredient(name = "Olive Oil", measurement = "1 tbs"),
            Ingredient(name = "Mushrooms", measurement = "300g"),
            Ingredient(name = "Chicken Legs", measurement = "4"),
            Ingredient(name = "Passata", measurement = "500g"),
            Ingredient(name = "Chicken Stock Cube", measurement = "1"),
            Ingredient(name = "Black Olives", measurement = "100g"),
            Ingredient(name = "Parsley", measurement = "Chopped"),

            )

        val ingredientsList3 = listOf(
            Ingredient(name = "Rice", measurement = "White"),
            Ingredient(name = "Onion", measurement = "1"),
            Ingredient(name = "Lime", measurement = "1"),
            Ingredient(name = "Garlic Clove", measurement = "3"),
            Ingredient(name = "Cucumber", measurement = "1"),
            Ingredient(name = "Carrots", measurement = "3 oz"),
            Ingredient(name = "Ground Beef", measurement = "1 lb"),
            Ingredient(name = "Soy Sauce", measurement = "2 oz"),
        )

        val ingredientsList4 = listOf(
            Ingredient(name = "Olive Oil", measurement = "2 tbs"),
            Ingredient(name = "Onion", measurement = "1 medium finely diced"),
            Ingredient(name = "Chickpeas", measurement = "250g"),
            Ingredient(name = "Vegetable Stock", measurement = "1.5L"),
            Ingredient(name = "Cumin", measurement = "1 tsp"),
            Ingredient(name = "Garlic", measurement = "5 cloves"),
            Ingredient(name = "Salt", measurement = "1\\/2 tsp"),
            Ingredient(name = "Harissa Spice", measurement = "1 tsp"),
            Ingredient(name = "Pepper", measurement = "Pinch"),
            Ingredient(name = "Lime", measurement = "1\\/2 "),

            )

        return listOf(ingredientsList1, ingredientsList2, ingredientsList3, ingredientsList4)
    }




}