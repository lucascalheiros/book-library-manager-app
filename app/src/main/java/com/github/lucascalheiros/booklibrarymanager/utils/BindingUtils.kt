package com.github.lucascalheiros.booklibrarymanager.utils

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.lucascalheiros.booklibrarymanager.R
import com.google.android.material.button.MaterialButton


@BindingAdapter("android:visibility")
fun View.bindVisibility(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

@BindingAdapter("android:checked")
fun MaterialButton.bindCheck(isChecked: Boolean) {
    this.isChecked = isChecked
}

@BindingAdapter("android:checkedAttrChanged")
fun MaterialButton.bindCheck(inverseBindingListener: InverseBindingListener?) {
    setOnClickListener {
        inverseBindingListener?.onChange()
    }
}

@InverseBindingAdapter(attribute = "android:checked")
fun MaterialButton.bindCheck(): Boolean {
    return isChecked
}

@BindingAdapter("layoutConstraintChangesAnimation")
fun ViewGroup.bindLayoutConstraintChangesAnimation(changes: Boolean) {
    if (changes) {
        val layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        this.layoutTransition = layoutTransition
    }
}

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