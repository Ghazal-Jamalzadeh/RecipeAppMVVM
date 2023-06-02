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
    fun getPopulars(queries : Map<String , String>) = viewModelScope.launch {
      popularLiveData.value = NetworkRequest.Loading()
        val response : Response<ResponseRecipes> = repository.remote.getRecipes(queries)
        popularLiveData.value = NetworkResponse(response).generalNetworkResponse()
    }


    //---Recent---//
    //Queries
    fun recentQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.TYPE] = Constants.MAIN_COURSE
        queries[Constants.DIET] = Constants.GLUTEN_FREE
        queries[Constants.NUMBER] = Constants.FULL_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    //Api
    val recentsLiveData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun getRecents(queries: Map<String, String>) = viewModelScope.launch {
        recentsLiveData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipes(queries)
        recentsLiveData.value = recentNetworkResponse(response)
    }


    private fun recentNetworkResponse(response: Response<ResponseRecipes>): NetworkRequest<ResponseRecipes> {
        return when {
            response.message().contains("timeout") -> NetworkRequest.Error("Timeout")
            response.code() == 401 -> NetworkRequest.Error("You are not authorized")
            response.code() == 402 -> NetworkRequest.Error("Your free plan finished")
            response.code() == 422 -> NetworkRequest.Error("Api key not found!")
            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.body()!!.results.isNullOrEmpty() -> NetworkRequest.Error("Not found any recipe!")
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}