package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {

    fun getMalsList(): MutableList<String> {
        return mutableListOf(
            "Main Course", "Bread", "Marinade", "Side Dish", "Breakfast", "Dessert", "Soup", "Snack", "Appetizer",
            "Beverage", "Drink", "Salad", "Sauce"
        )
    }

    fun getDietsList(): MutableList<String> {
        return mutableListOf("Gluten Free", "Ketogenic", "Vegetarian", "Vegan", "Pescetarian", "Paleo")
    }

}