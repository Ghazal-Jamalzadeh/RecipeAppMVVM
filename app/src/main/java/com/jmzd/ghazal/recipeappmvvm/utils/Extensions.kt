package com.jmzd.ghazal.recipeappmvvm.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message : String){
    Snackbar.make(this , message , Snackbar.LENGTH_SHORT).show()
}

fun RecyclerView.setupRecyclerview(myLayoutManager: RecyclerView.LayoutManager, myAdapter: RecyclerView.Adapter<*>) {
    this.apply {
        layoutManager = myLayoutManager
        setHasFixedSize(true) // برای زمانی که اندازه آیتم ها یکسان هستن و کوتاه بلند و اینا نیستن
        adapter = myAdapter
    }
}