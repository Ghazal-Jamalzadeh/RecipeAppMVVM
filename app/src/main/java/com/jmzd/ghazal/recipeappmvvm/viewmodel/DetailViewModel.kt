package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.*
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.DetailEntity
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.FavoriteEntity
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
    //--- API Detail ---//
    val detailLiveData = MutableLiveData<NetworkRequest<ResponseDetail>>()

    fun callDetailApi(id: Int, apiKey: String) = viewModelScope.launch {
        detailLiveData.value = NetworkRequest.Loading()
        val response: Response<ResponseDetail> = repository.remote.getDetail(id, apiKey)
        detailLiveData.value = NetworkResponse(response).generalNetworkResponse()
        //cache
        val cache = detailLiveData.value?.data
        if (cache != null)
            cacheDetail(cache.id!!, cache)
    }

    //--- API Similar ---//
    val similarData = MutableLiveData<NetworkRequest<ResponseSimilar>>()
    fun callSimilarApi(id: Int, apiKey: String) = viewModelScope.launch {
        similarData.value = NetworkRequest.Loading()
        val response = repository.remote.getSimilarRecipes(id, apiKey)
        similarData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //--- Cache Detail ---//
    private fun saveDetail(entity: DetailEntity) = viewModelScope.launch {
        repository.local.saveDetail(entity)
    }

    fun readDetailFromDb(id: Int): LiveData<DetailEntity> =
        repository.local.loadDetail(id).asLiveData()

    val existsDetailLiveData = MutableLiveData<Boolean>()
    fun existsDetail(id: Int) = viewModelScope.launch {
        repository.local.existsDetail(id).collect { isExist: Boolean ->
            existsDetailLiveData.postValue(isExist)
        }
    }

    private fun cacheDetail(recipeId: Int, detail: ResponseDetail) {
        saveDetail(DetailEntity(id = recipeId, result = detail))
    }

    //--- Favorite database ---//
    fun saveFavorite(entity: FavoriteEntity) = viewModelScope.launch {
        repository.local.saveFavorite(entity)
    }

    fun deleteFavorite(entity: FavoriteEntity) = viewModelScope.launch {
        repository.local.deleteFavorite(entity)
    }

    val existsFavoriteLiveData = MutableLiveData<Boolean>()
    fun existsFavorite(id: Int) = viewModelScope.launch {
        repository.local.existsFavorite(id).collect { existsFavoriteLiveData.postValue(it) }
    }

}