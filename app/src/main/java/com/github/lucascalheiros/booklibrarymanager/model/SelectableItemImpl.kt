package com.github.lucascalheiros.booklibrarymanager.model

import androidx.databinding.ObservableBoolean
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.SelectableItem

data class SelectableItemImpl<T>(
    override val name: String,
    override val value: T,
    override val isSelected: ObservableBoolean
) : SelectableItem<T>