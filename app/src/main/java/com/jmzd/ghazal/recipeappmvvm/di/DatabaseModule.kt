package com.jmzd.ghazal.recipeappmvvm.di

import android.content.Context
import androidx.room.Room
import com.jmzd.ghazal.recipeappmvvm.data.database.RecipeAppDao
import com.jmzd.ghazal.recipeappmvvm.data.database.RecipeAppDatabase
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : RecipeAppDatabase =
        Room.databaseBuilder(
        context, RecipeAppDatabase::class.java, Constants.DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(database: RecipeAppDatabase) : RecipeAppDao = database.dao()
}