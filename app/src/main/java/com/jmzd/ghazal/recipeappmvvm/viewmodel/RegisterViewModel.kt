package com.jmzd.ghazal.recipeappmvvm.viewmodel

import com.jmzd.ghazal.recipeappmvvm.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository){

}