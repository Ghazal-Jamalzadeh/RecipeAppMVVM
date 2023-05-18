package com.jmzd.ghazal.recipeappmvvm.di

import com.jmzd.ghazal.recipeappmvvm.models.register.BodyRegister
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object BodyModule {

    @Provides
    fun provideBodyRegister() : BodyRegister = BodyRegister()


}