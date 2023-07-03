package com.jmzd.ghazal.recipeappmvvm.data.repository

import com.jmzd.ghazal.recipeappmvvm.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class LuckyRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun getRandomRecipe(queries: Map<String, String>) = remote.getRandomRecipe(queries)
}