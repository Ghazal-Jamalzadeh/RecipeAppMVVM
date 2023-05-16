package com.jmzd.ghazal.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jmzd.ghazal.recipeappmvvm.data.repository.RegisterRepository
import com.jmzd.ghazal.recipeappmvvm.models.register.ResponseRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository){

    //API
    val registerLiveData  = MutableLiveData<ResponseRegister>()
}