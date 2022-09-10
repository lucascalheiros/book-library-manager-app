package com.github.lucascalheiros.booklibrarymanager.model.interfaces

import androidx.databinding.ObservableBoolean

interface SelectableItem<T> {
    val name: String
    val value: T
    val isSelected: ObservableBoolean
    override fun equals(other: Any?): Boolean
}