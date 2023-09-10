package com.github.lucascalheiros.common.utils.bindingUtils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


@BindingAdapter(value = ["url", "errorDrawable"])
fun ImageView.bindUrlImage(url: String?, errorDrawable: Drawable?) {
    val loadingDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
    Glide.with(this)
        .load(url)
        .placeholder(loadingDrawable)
        .fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(errorDrawable)
        .into(this)
}

