package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.recipeappmvvm.data.repository.SearchRepository
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    fun getSearchQueries(search: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.NUMBER] = Constants.FULL_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        queries[Constants.QUERY] = search
        return queries
    }

    val searchLiveData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    fun callSearchApi(queries: Map<String, String>) = viewModelScope.launch {
        searchLiveData.value = NetworkRequest.Loading()
        val response = repository.getSearchRecipe(queries)
        searchLiveData.value = NetworkResponse(response).generalNetworkResponse()
    }
}