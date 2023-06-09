package com.jmzd.ghazal.recipeappmvvm.models.lucky


import com.google.gson.annotations.SerializedName
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail

data class ResponseLucky(
    @SerializedName("recipes")
    val recipes: List<Recipe>?
) {
    data class Recipe(
        @SerializedName("aggregateLikes")
        val aggregateLikes: Int?, // 3
        @SerializedName("analyzedInstructions")
        val analyzedInstructions: List<ResponseDetail.AnalyzedInstruction>?,
        @SerializedName("cheap")
        val cheap: Boolean?, // false
        @SerializedName("cookingMinutes")
        val cookingMinutes: Int?, // -1
        @SerializedName("creditsText")
        val creditsText: String?, // Foodista.com – The Cooking Encyclopedia Everyone Can Edit
        @SerializedName("cuisines")
        val cuisines: List<Any?>?,
        @SerializedName("dairyFree")
        val dairyFree: Boolean?, // false
        @SerializedName("diets")
        val diets: List<String>?,
        @SerializedName("dishTypes")
        val dishTypes: List<String?>?,
        @SerializedName("extendedIngredients")
        val extendedIngredients: List<ResponseDetail.ExtendedIngredient>?,
        @SerializedName("gaps")
        val gaps: String?, // no
        @SerializedName("glutenFree")
        val glutenFree: Boolean?, // true
        @SerializedName("healthScore")
        val healthScore: Int?, // 15
        @SerializedName("id")
        val id: Int?, // 662463
        @SerializedName("image")
        val image: String?, // https://spoonacular.com/recipeImages/662463-556x370.jpg
        @SerializedName("imageType")
        val imageType: String?, // jpg
        @SerializedName("instructions")
        val instructions: String?, // Heat the fat in a 6-quart enameled Dutch oven over medium high heat and brown the ribs on all sides.  Pour in the stock; add water to cover if necessary.  Add the vegetable scraps and bring to a boil.  Lower the heat to a simmer, cover and cook for 45 minutes to an hour, or until the ribs are tender.While the ribs are braising, saute the onions over medium-low heat in a large cast iron skillet until soft; add the garlic and saute for another minute more.  Stir in tomato sauce, vinegar, honey, pepper or hot sauce, cayenne pepper, chili chili powder, salt and dry mustard. Bring the mixture to a boil, then reduce heat and simmer, uncovered for 25 minutes, stirring occasionally.When the ribs are tender, remove them from the braising liquid and add them to the spicy tomato sauce in the pan.  Raise the heat and cook, stirring frequently, until the sauce has glazed the ribs, about 10 minutes.  Serve with the remaining sauce on the side, if desired.
        @SerializedName("license")
        val license: String?, // CC BY 3.0
        @SerializedName("lowFodmap")
        val lowFodmap: Boolean?, // false
        @SerializedName("occasions")
        val occasions: List<Any?>?,
        @SerializedName("originalId")
        val originalId: Any?, // null
        @SerializedName("preparationMinutes")
        val preparationMinutes: Int?, // -1
        @SerializedName("pricePerServing")
        val pricePerServing: Double?, // 331.2
        @SerializedName("readyInMinutes")
        val readyInMinutes: Int?, // 45
        @SerializedName("servings")
        val servings: Int?, // 6
        @SerializedName("sourceName")
        val sourceName: String?, // Foodista
        @SerializedName("sourceUrl")
        val sourceUrl: String?, // https://www.foodista.com/recipe/2ZF5G885/sweet-and-spicy-country-style-ribs
        @SerializedName("spoonacularSourceUrl")
        val spoonacularSourceUrl: String?, // https://spoonacular.com/sweet-and-spicy-country-style-ribs-662463
        @SerializedName("summary")
        val summary: String?, // Sweet and Spicy Country-Style Ribs might be a good recipe to expand your main course repertoire. One portion of this dish contains about <b>25g of protein</b>, <b>20g of fat</b>, and a total of <b>506 calories</b>. This gluten free recipe serves 6 and costs <b>$3.31 per serving</b>. Head to the store and pick up beef stock, honey, butter, and a few other things to make it today. 3 people found this recipe to be scrumptious and satisfying. It is brought to you by Foodista. From preparation to the plate, this recipe takes around <b>45 minutes</b>. Overall, this recipe earns a <b>good spoonacular score of 46%</b>. Try <a href="https://spoonacular.com/recipes/sweet-and-sour-country-style-ribs-487238">Sweet and Sour Country Style Ribs</a>, <a href="https://spoonacular.com/recipes/sweet-n-spicy-country-ribs-397391">Sweet 'n' Spicy Country Ribs</a>, and <a href="https://spoonacular.com/recipes/country-style-ribs-146904">Country Style Ribs</a> for similar recipes.
        @SerializedName("sustainable")
        val sustainable: Boolean?, // false
        @SerializedName("title")
        val title: String?, // Sweet and Spicy Country-Style Ribs
        @SerializedName("vegan")
        val vegan: Boolean?, // false
        @SerializedName("vegetarian")
        val vegetarian: Boolean?, // false
        @SerializedName("veryHealthy")
        val veryHealthy: Boolean?, // false
        @SerializedName("veryPopular")
        val veryPopular: Boolean?, // false
        @SerializedName("weightWatcherSmartPoints")
        val weightWatcherSmartPoints: Int? // 20
    )
}