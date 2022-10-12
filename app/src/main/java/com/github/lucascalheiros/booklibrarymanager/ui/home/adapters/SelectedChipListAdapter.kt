package com.github.lucascalheiros.booklibrarymanager.ui.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.lucascalheiros.booklibrarymanager.databinding.ItemSelectedTagChipBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class SelectedChipsListAdapter :
    ListAdapter<String, SelectedChipsListAdapter.ItemTagChipsViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }

    var onChange: (() -> Unit)? = null

    val updatedList = mutableListOf<String>()

    override fun submitList(list: MutableList<String>?) {
        updatedList.clear()
        updatedList.addAll(list.orEmpty())
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTagChipsViewHolder {
        val binding = ItemSelectedTagChipBinding.inflate(LayoutInflater.from(parent.context))
        return ItemTagChipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTagChipsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemTagChipsViewHolder(private val binding: ItemSelectedTagChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(name: String) {
            binding.name = name
            binding.chip.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    submitList(currentList.toMutableList().apply {
                        remove(name)
                    })
                    onChange?.invoke()
                }
                true
            }
        }
    }
}

@BindingAdapter(
    value = [
        "selectedChipsListAdapterOptions",
        "selectedChipsListAdapterUseCustomLayoutManager",
        "selectedChipsListAdapterOptionsAttrChanged"
    ],
    requireAll = false
)
fun RecyclerView.bindSelectedChipsListAdapter(
    selectedOptions: List<String>?,
    customLayoutManager: Boolean?,
    attrChanged: InverseBindingListener? = null
) {
    adapter.let { rvAdapter ->
        if (rvAdapter !is SelectedChipsListAdapter) {
            if (customLayoutManager != true) {
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.CENTER
                this.layoutManager = layoutManager
            }
            SelectedChipsListAdapter().also {
                adapter = it
            }
        } else rvAdapter
    }.let { adapter ->
        adapter.onChange = { attrChanged?.onChange() }
        adapter.submitList(selectedOptions.orEmpty().toMutableList())
    }
}

@InverseBindingAdapter(
    attribute = "selectedChipsListAdapterOptions",
    event = "selectedChipsListAdapterOptionsAttrChanged"
)
fun RecyclerView.inverseBindSelectedChipsListAdapterOptions(): List<String> {
    return adapter.let { adapter ->
        if (adapter is SelectedChipsListAdapter) {
            adapter.updatedList
        } else listOf()
    }
}

