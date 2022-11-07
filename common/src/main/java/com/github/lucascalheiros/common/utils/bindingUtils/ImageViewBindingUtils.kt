package com.github.lucascalheiros.common.utils.bindingUtils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.lucascalheiros.common.R


@BindingAdapter("url")
fun ImageView.bindUrlImage(url: String?) {
    val loadingDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
    Glide.with(this).load(url).placeholder(loadingDrawable).fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.ic_broken_image)
        .into(this)
}

