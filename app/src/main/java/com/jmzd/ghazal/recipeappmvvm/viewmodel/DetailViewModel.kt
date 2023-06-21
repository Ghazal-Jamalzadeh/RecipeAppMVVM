package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.jmzd.ghazal.recipeappmvvm.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {

}