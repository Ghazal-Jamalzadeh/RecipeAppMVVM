package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.recipeappmvvm.data.repository.MenuRepository
import com.jmzd.ghazal.recipeappmvvm.models.menu.MenuStoredModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: MenuRepository) : ViewModel() {

    fun saveToStore(meal: String, mealId: Int, diet: String, dietId: Int) = viewModelScope.launch(
        Dispatchers.IO) {
        repository.saveMenuData(meal, diet, mealId ,  dietId)
    }

    val menuStoredItems : Flow<MenuStoredModel> = repository.menuDataFlow

    fun getMealsList(): MutableList<String> {
        return mutableListOf(
            "Main Course", "Bread", "Marinade", "Side Dish", "Breakfast", "Dessert", "Soup", "Snack", "Appetizer",
            "Beverage", "Drink", "Salad", "Sauce"
        )
    }

    fun getDietsList(): MutableList<String> {
        return mutableListOf("Gluten Free", "Ketogenic", "Vegetarian", "Vegan", "Pescetarian", "Paleo")
    }
}