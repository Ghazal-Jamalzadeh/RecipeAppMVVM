package com.jmzd.ghazal.recipeappmvvm.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jmzd.ghazal.recipeappmvvm.data.network.ApiServices
import com.jmzd.ghazal.recipeappmvvm.utils.Constants.BASE_URL
import com.jmzd.ghazal.recipeappmvvm.utils.Constants.CONNECTION_TIME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProviders {

    @Provides
    @Singleton
    fun provideBaseUrl() : String = BASE_URL

    @Provides
    @Singleton
    fun provideNetworkTime() : Long = CONNECTION_TIME

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideInterceptor() : HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideClient(time: Long, interceptor: HttpLoggingInterceptor) : OkHttpClient =
        OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .writeTimeout(time, TimeUnit.SECONDS)
        .readTimeout(time, TimeUnit.SECONDS)
        .connectTimeout(time, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)
}