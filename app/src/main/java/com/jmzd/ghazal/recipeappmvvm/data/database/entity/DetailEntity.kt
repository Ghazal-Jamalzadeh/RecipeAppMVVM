package com.jmzd.ghazal.recipeappmvvm.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail
import com.jmzd.ghazal.recipeappmvvm.utils.Constants

@Entity(tableName = Constants.DETAIL_TABLE_NAME)
data class DetailEntity (
    @PrimaryKey(autoGenerate = false)
    val id : Int ,
    val result: ResponseDetail
        )