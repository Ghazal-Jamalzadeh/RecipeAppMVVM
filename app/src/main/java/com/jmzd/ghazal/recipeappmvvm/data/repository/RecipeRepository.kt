package com.jmzd.ghazal.recipeappmvvm.data.repository

import com.jmzd.ghazal.recipeappmvvm.data.source.LocalDataSource
import com.jmzd.ghazal.recipeappmvvm.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepository @Inject constructor(remoteDataSource: RemoteDataSource , localDataSource: LocalDataSource) {
    val remote = remoteDataSource
    val local = localDataSource
}