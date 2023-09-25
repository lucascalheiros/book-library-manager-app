package com.github.lucascalheiros.feature_home.presentation.home.adapters

import androidx.databinding.ObservableBoolean
import com.github.lucascalheiros.feature_home.presentation.home.adapters.interfaces.SelectableItem

data class SelectableItemImpl<T>(
    override val name: String,
    override val value: T,
    override val isSelected: ObservableBoolean
) : SelectableItem<T>