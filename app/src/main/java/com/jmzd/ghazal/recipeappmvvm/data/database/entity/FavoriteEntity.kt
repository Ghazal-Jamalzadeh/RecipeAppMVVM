package com.jmzd.ghazal.recipeappmvvm.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail
import com.jmzd.ghazal.recipeappmvvm.utils.Constants

@Entity(tableName = Constants.FAVORITE_TABLE_NAME)
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = false)
    val id : Int ,
    val result: ResponseDetail
)