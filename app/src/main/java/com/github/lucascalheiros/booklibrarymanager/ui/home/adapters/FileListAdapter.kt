package com.github.lucascalheiros.booklibrarymanager.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.lucascalheiros.booklibrarymanager.databinding.ItemFileListBinding
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener

class FileListAdapter: ListAdapter<FileListItem, FileListAdapter.FileListItemViewHolder>(Diff) {

    object Diff: DiffUtil.ItemCallback<FileListItem>() {
        override fun areItemsTheSame(oldItem: FileListItem, newItem: FileListItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FileListItem, newItem: FileListItem): Boolean {
            return oldItem == newItem
        }
    }

    var listener: FileListItemListener? = null
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

    class FileListItemViewHolder(private val binding: ItemFileListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FileListItem, listener: FileListItemListener?) {
            binding.model = item
            binding.listener = listener ?: object : FileListItemListener {
                override fun download(item: FileListItem) = Unit
                override fun read(item: FileListItem) = Unit
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["fileListAdapterItems", "fileListAdapterListener"], requireAll = false)
        fun bind(rv: RecyclerView, items: List<FileListItem>?, listener: FileListItemListener?) {
            rv.adapter?.let {
                if (it !is FileListAdapter) {
                    rv.adapter = FileListAdapter()
                }
            } ?: run {
                rv.adapter = FileListAdapter()
            }
            (rv.adapter as FileListAdapter).let { adapter ->
                adapter.listener = listener
                adapter.submitList(items)
            }
        }
    }
}
