package com.jmzd.ghazal.recipeappmvvm.viewmodel

import android.telephony.SignalStrength
import androidx.lifecycle.*
import com.jmzd.ghazal.recipeappmvvm.data.database.RecipeEntity
import com.jmzd.ghazal.recipeappmvvm.data.repository.MenuRepository
import com.jmzd.ghazal.recipeappmvvm.data.repository.RecipeRepository
import com.jmzd.ghazal.recipeappmvvm.models.menu.MenuStoredModel
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository, private val menuRepository: MenuRepository,
) : ViewModel() {

    //---popular---//
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
    fun callPopularApi(queries: Map<String, String>) = viewModelScope.launch {
        popularLiveData.value = NetworkRequest.Loading()
        val response: Response<ResponseRecipes> = repository.remote.getRecipes(queries)
        popularLiveData.value = NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache: ResponseRecipes? = popularLiveData.value?.data
        if (cache != null) {
            offlinePopular(cache)
        }
    }

    //Local
    private fun savePopular(entity: RecipeEntity): Job = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val popularFromDbLiveData: LiveData<List<RecipeEntity>> =
        repository.local.loadRecipes().asLiveData()

    private fun offlinePopular(response: ResponseRecipes) {
        val entity = RecipeEntity(0, response)
        savePopular(entity)
    }

    //---Recent---//
    val recentsLiveData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    //Queries
    private var mealType = Constants.MAIN_COURSE
    private var dietType = Constants.GLUTEN_FREE

    fun getRecentQueries(): HashMap<String, String> {

        viewModelScope.launch {
            menuRepository.menuDataFlow.collect { stoedData: MenuStoredModel ->
                mealType = stoedData.meal
                dietType = stoedData.diet
            }
        }

        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.TYPE] = mealType
        queries[Constants.DIET] = dietType
        queries[Constants.NUMBER] = Constants.FULL_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    //Api
    fun callRecentApi(queries: Map<String, String>) = viewModelScope.launch {
        recentsLiveData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipes(queries)
        recentsLiveData.value = recentNetworkResponse(response)
        //Cache
        val cache: ResponseRecipes? = recentsLiveData.value?.data
        if (cache != null) {
            offlineRecent(cache)
        }
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

    //Local
    private fun saveRecent(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val readFromDbLiveData = repository.local.loadRecipes().asLiveData()

    private fun offlineRecent(response: ResponseRecipes) {
        val entity = RecipeEntity(1, response)
        saveRecent(entity)
    }

}