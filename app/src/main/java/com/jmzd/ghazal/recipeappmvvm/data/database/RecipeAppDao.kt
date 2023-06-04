package com.jmzd.ghazal.recipeappmvvm.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    //Recipe
    @Insert(onConflict = REPLACE)
    suspend fun saveRecipes(entity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY ID ASC")
    fun loadRecipes(): Flow<List<RecipeEntity>>
}