package com.github.lucascalheiros.feature_home.presentation.home.adapters

import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*

import com.github.lucascalheiros.feature_home.presentation.home.handlers.BookLibFileItemListener
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import com.github.lucascalheiros.feature_home.R
import com.github.lucascalheiros.feature_home.databinding.ItemFileListBinding


class BookLibFileListAdapter :
    ListAdapter<BookLibFile, BookLibFileListAdapter.FileListItemViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<BookLibFile>() {
        override fun areItemsTheSame(oldItem: BookLibFile, newItem: BookLibFile): Boolean {
            return oldItem.localId == newItem.localId || (oldItem.cloudId != null && oldItem.cloudId == newItem.cloudId )
        }

        override fun areContentsTheSame(oldItem: BookLibFile, newItem: BookLibFile): Boolean {
            return oldItem == newItem
        }
    }

    var listener: BookLibFileItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListItemViewHolder {
        val binding = ItemFileListBinding.inflate(LayoutInflater.from(parent.context))
        return FileListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FileListItemViewHolder(private val binding: ItemFileListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookLibFile) {
            binding.model = item
            binding.listener = listener ?: object : BookLibFileItemListener {
                override fun read(item: BookLibFile) = Unit
                override fun download(item: BookLibFile) = Unit
                override fun edit(item: BookLibFile) = Unit
                override fun delete(item: BookLibFile) = Unit
            }
            binding.options.setOnClickListener {
                showMenu(it, R.menu.file_options, item, listener)
            }
        }

        private fun showMenu(
            v: View,
            @MenuRes menuRes: Int,
            file: BookLibFile,
            listener: BookLibFileItemListener?
        ) {
            val popup = PopupMenu(v.context, v)
            popup.menuInflater.inflate(menuRes, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuEdit -> listener?.edit(file)
                    R.id.menuDownload -> listener?.download(file)
                    R.id.menuDelete -> listener?.delete(file)
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
            value = ["fileListAdapterItems", "fileListAdapterListener"],
            requireAll = false
        )
        fun RecyclerView.bindFileListAdapter(
            items: List<BookLibFile>?,
            bookLibFileItemListener: BookLibFileItemListener?
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
                listener = bookLibFileItemListener
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
