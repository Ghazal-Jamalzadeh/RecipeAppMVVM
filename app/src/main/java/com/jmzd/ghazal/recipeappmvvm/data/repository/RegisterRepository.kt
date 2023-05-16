package com.jmzd.ghazal.recipeappmvvm.data.repository

import android.content.Context
import com.jmzd.ghazal.recipeappmvvm.data.source.RemoteDataSource
import com.jmzd.ghazal.recipeappmvvm.models.register.BodyRegister
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remote: RemoteDataSource
) {
    //API
    suspend fun postRegister(apiKey: String, body: BodyRegister) = remote.postRegister(apiKey, body)
}