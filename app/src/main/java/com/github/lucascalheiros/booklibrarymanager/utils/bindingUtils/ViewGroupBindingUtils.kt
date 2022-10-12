package com.github.lucascalheiros.booklibrarymanager.utils.bindingUtils

import android.animation.LayoutTransition
import android.view.ViewGroup
import androidx.databinding.BindingAdapter


@BindingAdapter("layoutConstraintChangesAnimation")
fun ViewGroup.bindLayoutConstraintChangesAnimation(changes: Boolean) {
    if (changes) {
        val layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        this.layoutTransition = layoutTransition
    }
}
