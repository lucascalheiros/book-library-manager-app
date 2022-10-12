package com.github.lucascalheiros.booklibrarymanager.ui.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.lucascalheiros.booklibrarymanager.databinding.ItemSelectableTagChipBinding
import com.github.lucascalheiros.booklibrarymanager.model.SelectableItemImpl
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.SelectableItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class SelectableChipsListAdapter :
    ListAdapter<SelectableItem<String>, SelectableChipsListAdapter.ItemTagChipsViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<SelectableItem<String>>() {
        override fun areItemsTheSame(
            oldItem: SelectableItem<String>,
            newItem: SelectableItem<String>
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SelectableItem<String>,
            newItem: SelectableItem<String>
        ): Boolean {
            return oldItem == newItem
        }
    }

    var onChange: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTagChipsViewHolder {
        val binding = ItemSelectableTagChipBinding.inflate(LayoutInflater.from(parent.context))
        return ItemTagChipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTagChipsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemTagChipsViewHolder(private val binding: ItemSelectableTagChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: SelectableItem<String>) {
            binding.item = item
            binding.chip.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    item.isSelected.set(!item.isSelected.get())
                    onChange?.invoke()
                }
                true
            }
        }
    }
}

@BindingAdapter(
    value = [
        "chipsListAdapterOptions",
        "chipsListAdapterSelectedOptions",
        "chipsListAdapterUseCustomLayoutManager",
        "chipsListAdapterSelectedOptionsAttrChanged"
    ],
    requireAll = false
)
fun RecyclerView.bindChipsListAdapter(
    options: List<String>?,
    selectedOptions: List<String>?,
    customLayoutManager: Boolean?,
    attrChanged: InverseBindingListener? = null
) {
    adapter.let { rvAdapter ->
        if (rvAdapter !is SelectableChipsListAdapter) {
            if (customLayoutManager != true) {
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.CENTER
                this.layoutManager = layoutManager
            }
            SelectableChipsListAdapter().also {
                adapter = it
            }
        } else rvAdapter
    }.let { adapter ->
        val optionNameMap: Map<String, List<SelectableItem<String>>> = adapter.currentList.groupBy { it.name }
        options.orEmpty().map { option ->
            (optionNameMap[option]?.firstOrNull() ?: SelectableItemImpl(
                option,
                option,
                ObservableBoolean(false)
            )).also {
                it.isSelected.set(selectedOptions?.contains(option) == true)
            }
        }.let {
            adapter.onChange = { attrChanged?.onChange() }
            adapter.submitList(it)
        }
    }
}

@InverseBindingAdapter(
    attribute = "chipsListAdapterSelectedOptions",
    event = "chipsListAdapterSelectedOptionsAttrChanged"
)
fun RecyclerView.inverseBindChipsListAdapterSelectedOptions(): List<String> {
    return adapter.let { adapter ->
        if (adapter is SelectableChipsListAdapter) {
            adapter.currentList.filter { it.isSelected.get() }.map { it.value }
        } else listOf()
    }
}

