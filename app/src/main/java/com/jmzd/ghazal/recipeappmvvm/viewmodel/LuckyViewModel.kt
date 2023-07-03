package com.jmzd.ghazal.recipeappmvvm.viewmodel

import com.jmzd.ghazal.recipeappmvvm.data.repository.LuckyRepository
import com.jmzd.ghazal.recipeappmvvm.models.lucky.ResponseLucky
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LuckyViewModel @Inject constructor(private val repository: LuckyRepository) : ViewModel() {

    fun getLuckyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.NUMBER] = "1"
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    val luckyLiveData = MutableLiveData<NetworkRequest<ResponseLucky>>()

    fun callLuckyApi(queries: Map<String, String>) = viewModelScope.launch {
        luckyLiveData.value = NetworkRequest.Loading()
        val response = repository.getRandomRecipe(queries)
        luckyLiveData.value = NetworkResponse(response).generalNetworkResponse()
    }
}