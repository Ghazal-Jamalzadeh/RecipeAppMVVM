package com.jmzd.ghazal.recipeappmvvm.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.DetailEntity
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.RecipeEntity
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    //Recipe
    @Insert(onConflict = REPLACE)
    suspend fun saveRecipes(entity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY ID ASC")
    fun loadRecipes(): Flow<List<RecipeEntity>>

    //Detail
    @Insert(onConflict = REPLACE)
    suspend fun saveDetail(entity: DetailEntity)

    @Query("SELECT * FROM ${Constants.DETAIL_TABLE_NAME} WHERE id = :id")
    fun loadDetailById(id: Int): Flow<DetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.DETAIL_TABLE_NAME} WHERE ID = :id)")
    fun existsDetail(id: Int): Flow<Boolean>


}