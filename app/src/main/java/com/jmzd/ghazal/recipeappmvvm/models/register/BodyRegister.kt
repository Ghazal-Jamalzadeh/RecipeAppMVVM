package com.jmzd.ghazal.recipeappmvvm.models.register


import com.google.gson.annotations.SerializedName

data class BodyRegister(
    @SerializedName("email")
    var email: String = "", // your user's email
    @SerializedName("firstName")
    var firstName: String = "", // your user's first name
    @SerializedName("lastName")
    var lastName: String = "", // your user's last name
    @SerializedName("username")
    var username: String = "" // your user's name
)