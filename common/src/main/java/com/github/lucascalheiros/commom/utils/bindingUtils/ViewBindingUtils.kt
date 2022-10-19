package com.github.lucascalheiros.common.utils.bindingUtils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("android:visibility")
fun View.bindVisibility(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["visibilityLeftSlideInOut"])
fun View.bindVisibilityLeftSlideInOut(
    visibility: Boolean
) {
    val duration = 500L
    if (visibility) {
        translationX = (resources.displayMetrics.widthPixels.toFloat())
        setVisibility(View.VISIBLE)
        animate()
            .translationX(-1f)
            .setDuration(duration)
            .setListener(null)
    } else {
        if (translationX.toInt() != -1) {
            setVisibility(View.GONE)
        }
        translationX = (0F)
        animate()
            .translationX(width.toFloat())
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    setVisibility(View.GONE)
                }
            })
    }
}