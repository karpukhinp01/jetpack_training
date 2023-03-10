package com.example.kotlinjetpackdogs.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinjetpackdogs.R

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable((context)).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions().placeholder(progressDrawable).error(R.drawable.ic_launcher_foreground)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .optionalFitCenter()
        .into(this)
}