package com.jmzd.ghazal.recipeappmvvm.data.source

import com.jmzd.ghazal.recipeappmvvm.data.database.RecipeAppDao
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {

    //Recipes
    suspend fun saveRecipes(entity: RecipeEntity) : Unit = dao.saveRecipes(entity)
    fun loadRecipes() : Flow<List<RecipeEntity>> = dao.loadRecipes()


}