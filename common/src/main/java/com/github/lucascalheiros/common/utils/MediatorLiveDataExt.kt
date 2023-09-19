package com.github.lucascalheiros.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> MediatorLiveData<T>.addSources(vararg sources: LiveData<*>, onChanged:  () -> Unit) {
    sources.forEach {
        addSource(it) {
            onChanged.invoke()
        }
    }
}