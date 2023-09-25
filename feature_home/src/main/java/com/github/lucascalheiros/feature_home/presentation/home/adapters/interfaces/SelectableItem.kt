package com.github.lucascalheiros.feature_home.presentation.home.adapters.interfaces

import androidx.databinding.ObservableBoolean

interface SelectableItem<T> {
    val name: String
    val value: T
    val isSelected: ObservableBoolean
    override fun equals(other: Any?): Boolean
}