package com.github.lucascalheiros.feature_home.presentation.home.adapters

import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.lucascalheiros.feature_home.R
import com.github.lucascalheiros.feature_home.databinding.ItemFileListBinding
import com.github.lucascalheiros.feature_home.presentation.home.model.BookLibUiModel


class BookLibFileListAdapter :
    ListAdapter<BookLibUiModel, BookLibFileListAdapter.FileListItemViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<BookLibUiModel>() {
        override fun areItemsTheSame(oldItem: BookLibUiModel, newItem: BookLibUiModel): Boolean {
            return oldItem.localId == newItem.localId || (oldItem.cloudId != null && oldItem.cloudId == newItem.cloudId )
        }

        override fun areContentsTheSame(oldItem: BookLibUiModel, newItem: BookLibUiModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListItemViewHolder {
        val binding = ItemFileListBinding.inflate(LayoutInflater.from(parent.context))
        return FileListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FileListItemViewHolder(private val binding: ItemFileListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookLibUiModel) {
            binding.model = item
            binding.options.setOnClickListener {
                showMenu(it, R.menu.file_options, item)
            }
        }

        private fun showMenu(
            v: View,
            @MenuRes menuRes: Int,
            file: BookLibUiModel,
        ) {
            val popup = PopupMenu(v.context, v)
            popup.menuInflater.inflate(menuRes, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuEdit -> file.onEdit()
                    R.id.menuDownload -> file.onDownload()
                    R.id.menuDelete -> file.onDelete()
                    else -> {}
                }
                true
            }
            if (popup.menu is MenuBuilder) {
                val menuBuilder = popup.menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)
                for (item in menuBuilder.visibleItems) {
                    if (item.icon != null) {
                        item.icon = InsetDrawable(item.icon, 0, 0, 0, 0)
                    }
                }
            }
            popup.show()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter(
            value = ["fileListAdapterItems"],
            requireAll = false
        )
        fun RecyclerView.bindFileListAdapter(
            items: List<BookLibUiModel>?
        ) {
            val bookLibAdapter = adapter
            if (bookLibAdapter !is BookLibFileListAdapter) {
                addDividerItemDecoration()
                BookLibFileListAdapter().also {
                    adapter = it
                }
            } else {
                bookLibAdapter
            }.run {
                submitList(items)
            }
        }

        private fun RecyclerView.addDividerItemDecoration() {
            layoutManager.let {
                if (it is LinearLayoutManager) {
                    val dividerItemDecoration = DividerItemDecoration(
                        context,
                        it.orientation
                    )
                    addItemDecoration(dividerItemDecoration)
                }
            }
        }
    }
}
