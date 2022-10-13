package com.github.lucascalheiros.commom.utils.bindingUtils

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.button.MaterialButton


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
