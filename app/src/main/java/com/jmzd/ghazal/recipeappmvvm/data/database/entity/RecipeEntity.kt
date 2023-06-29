package com.jmzd.ghazal.recipeappmvvm.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes
import com.jmzd.ghazal.recipeappmvvm.utils.Constants

@Entity(tableName = Constants.RECIPE_TABLE_NAME)
data class RecipeEntity (
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0 ,
    var response : ResponseRecipes
        )