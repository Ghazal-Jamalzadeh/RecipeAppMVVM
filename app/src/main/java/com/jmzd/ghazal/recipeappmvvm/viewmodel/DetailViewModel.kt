package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.recipeappmvvm.data.repository.RecipeRepository
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseSimilar
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {
    //Api
    val detailLiveData = MutableLiveData<NetworkRequest<ResponseDetail>>()

    fun callDetailApi(id: Int, apiKey: String) = viewModelScope.launch {
        detailLiveData.value = NetworkRequest.Loading()
        val response : Response<ResponseDetail> = repository.remote.getDetail(id, apiKey)
        detailLiveData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //Similar
    val similarData = MutableLiveData<NetworkRequest<ResponseSimilar>>()
    fun callSimilarApi(id: Int, apiKey: String) = viewModelScope.launch {
        similarData.value = NetworkRequest.Loading()
        val response = repository.remote.getSimilarRecipes(id, apiKey)
        similarData.value = NetworkResponse(response).generalNetworkResponse()
    }
}