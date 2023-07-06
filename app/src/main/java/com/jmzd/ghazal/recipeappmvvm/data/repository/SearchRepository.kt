package com.jmzd.ghazal.recipeappmvvm.data.repository

import com.jmzd.ghazal.recipeappmvvm.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun getSearchRecipe(queries: Map<String, String>) = remote.getRecipes(queries)
}