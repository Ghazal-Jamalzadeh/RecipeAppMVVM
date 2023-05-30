package com.jmzd.ghazal.recipeappmvvm.viewmodel

import android.telephony.SignalStrength
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.recipeappmvvm.data.repository.RecipeRepository
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel(){

    //popular
    val popularLiveData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    //Queries
    fun getPopularQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.SORT] = Constants.POPULARITY
        queries[Constants.NUMBER] = Constants.LIMITED_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }
    //API
    fun GetPopulars(queries : Map<String , String>) = viewModelScope.launch {
      popularLiveData.value = NetworkRequest.Loading()
        val response : Response<ResponseRecipes> = repository.remote.getRecipes(queries)
        popularLiveData.value = NetworkResponse(response).generalNetworkResponse()
    }
}