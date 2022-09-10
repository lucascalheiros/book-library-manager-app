package com.github.lucascalheiros.booklibrarymanager.utils

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
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