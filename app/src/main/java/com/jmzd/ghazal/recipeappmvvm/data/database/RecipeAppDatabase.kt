package com.jmzd.ghazal.recipeappmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.RecipeEntity

@Database(entities = [RecipeEntity::class , RecipeEntity:: class ] , version = 2 , exportSchema = false)
@TypeConverters(RecipeAppTypeConverter::class)
abstract class RecipeAppDatabase : RoomDatabase() {
    abstract fun dao() : RecipeAppDao
}