package com.jmzd.ghazal.recipeappmvvm.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.jmzd.ghazal.recipeappmvvm.data.repository.RegisterRepository
import com.jmzd.ghazal.recipeappmvvm.models.register.BodyRegister
import com.jmzd.ghazal.recipeappmvvm.models.register.ResponseRegister
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkResponse
import kotlinx.coroutines.launch
import retrofit2.Response

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository) : ViewModel(){

    //API
    val registerLiveData = MutableLiveData<NetworkRequest<ResponseRegister>>()
    fun callRegisterApi(apiKey: String, body: BodyRegister) = viewModelScope.launch {
        registerLiveData.value = NetworkRequest.Loading()
        val response  : Response<ResponseRegister> = repository.postRegister(apiKey, body)
        registerLiveData.value = NetworkResponse(response).generalNetworkResponse()
    }
}