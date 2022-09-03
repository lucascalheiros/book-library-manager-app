package com.github.lucascalheiros.booklibrarymanager.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import com.github.lucascalheiros.booklibrarymanager.databinding.ItemFileListBinding
import com.github.lucascalheiros.booklibrarymanager.model.handlers.BookLibFileItemListener
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile


class BookLibFileListAdapter :
    ListAdapter<BookLibFile, BookLibFileListAdapter.FileListItemViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<BookLibFile>() {
        override fun areItemsTheSame(oldItem: BookLibFile, newItem: BookLibFile): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BookLibFile, newItem: BookLibFile): Boolean {
            return oldItem == newItem
        }
    }

    var listener: BookLibFileItemListener? = null
        set(value) {
            if (value != field) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListItemViewHolder {
        val binding = ItemFileListBinding.inflate(LayoutInflater.from(parent.context))
        return FileListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileListItemViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class FileListItemViewHolder(private val binding: ItemFileListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookLibFile, listener: BookLibFileItemListener?) {
            binding.model = item
            binding.listener = listener ?: object : BookLibFileItemListener {
                override fun download(item: BookLibFile) = Unit
                override fun read(item: BookLibFile) = Unit
            }
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
            listener: BookLibFileItemListener?
        ) {
            adapter.let { rvAdapter ->
                if (rvAdapter !is BookLibFileListAdapter) {
                    layoutManager.let { layoutManager ->
                        if (layoutManager is LinearLayoutManager) {
                            val dividerItemDecoration = DividerItemDecoration(
                                context,
                                layoutManager.orientation
                            )
                            addItemDecoration(dividerItemDecoration)
                        }
                    }
                    BookLibFileListAdapter().also {
                        adapter = it
                    }
                } else rvAdapter
            }.let {
                it.listener = listener
                it.submitList(items)
            }
        }
    }
}
