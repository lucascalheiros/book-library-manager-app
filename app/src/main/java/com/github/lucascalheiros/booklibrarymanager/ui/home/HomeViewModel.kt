package com.github.lucascalheiros.booklibrarymanager.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItemImpl
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItemListener
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

@KoinViewModel
class HomeViewModel(
) : ViewModel() {

    val fileItems = MutableLiveData<List<FileListItem>>()
    val fileItemListener = MutableLiveData<FileListItemListener>()

    init {
        val items = (0..10).map {
            FileListItemImpl(
                "name $it",
                (0..it).map { n -> "tag $n" },
                LocalDateTime.now(),
                LocalDateTime.now(),
                "$it",
                it/10f
            )
        }
        fileItems.value = items
        fileItemListener.value = object : FileListItemListener {
            override fun download(item: FileListItem) {
                Log.d("testing, download", item.toString())
            }

            override fun read(item: FileListItem) {
                Log.d("read, edit", item.toString())
            }

        }
    }
}