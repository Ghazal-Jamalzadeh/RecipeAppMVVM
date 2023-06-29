package com.jmzd.ghazal.recipeappmvvm.models.detail


import com.google.gson.annotations.SerializedName
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseSimilar.ResponseSimilarItem

class ResponseSimilar : ArrayList<ResponseSimilarItem>(){
    data class ResponseSimilarItem(
        @SerializedName("id")
        val id: Int?, // 132855
        @SerializedName("imageType")
        val imageType: String?, // jpg
        @SerializedName("readyInMinutes")
        val readyInMinutes: Int?, // 70
        @SerializedName("servings")
        val servings: Int?, // 6
        @SerializedName("sourceUrl")
        val sourceUrl: String?, // http://www.food.com/recipe/zaatar-bread-rolls-manaiesh-bi-zaatar-142472
        @SerializedName("title")
        val title: String? // Za'atar Bread Rolls (Manaiesh Bi Za'atar)
    )
}