package com.jmzd.ghazal.recipeappmvvm.data.network


import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseSimilar
import com.jmzd.ghazal.recipeappmvvm.models.lucky.ResponseLucky
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes
import com.jmzd.ghazal.recipeappmvvm.models.register.BodyRegister
import com.jmzd.ghazal.recipeappmvvm.models.register.ResponseRegister
import com.jmzd.ghazal.recipeappmvvm.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {
    @POST("users/connect")
    suspend fun postRegister(@Query(API_KEY) apiKey: String, @Body body: BodyRegister): Response<ResponseRegister>

    @GET("recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<ResponseRecipes>

    @GET("recipes/{id}/information")
    suspend fun getDetail(@Path("id") recipeId: Int, @Query(API_KEY) apiKey: String): Response<ResponseDetail>

    @GET("recipes/{id}/similar")
    suspend fun getSimilarRecipes(@Path("id") recipeId: Int, @Query(API_KEY) apiKey: String): Response<ResponseSimilar>

    @GET("recipes/random")
    suspend fun getRandomRecipe(@QueryMap queries: Map<String, String>): Response<ResponseLucky>
}