package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jmzd.ghazal.recipeappmvvm.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(repository: FavoritesRepository) : ViewModel() {
    val favoritesLiveData = repository.local.loadFavorites().asLiveData()
}