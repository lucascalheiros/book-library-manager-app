package com.github.lucascalheiros.booklibrarymanager.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.useCase.FileListUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.io.File

@KoinViewModel
class HomeViewModel(
    private val fileListUseCase: FileListUseCase
) : ViewModel() {

    val fileItems = MutableLiveData<List<FileListItem>>()

    val fileHandlerRequestState =
        MutableLiveData<FileHandlerRequestState>(FileHandlerRequestState.Idle)

    val fileItemListener = MutableLiveData<FileListItemListener>(object : FileListItemListener {
        override fun download(item: FileListItem) {
            viewModelScope.launch {
                item.id?.let {
                    fileHandlerRequestState.value =
                        FileHandlerRequestState.DownloadFile(fileListUseCase.getFile(it))
                }
            }
        }

        override fun read(item: FileListItem) {
            viewModelScope.launch {
                item.id?.let {
                    fileHandlerRequestState.value =
                        FileHandlerRequestState.ReadFile(it)
                }
            }
        }
    })

    fun handleFileHandlerRequestState() {
        fileHandlerRequestState.value = FileHandlerRequestState.Idle
    }

    init {
        viewModelScope.launch {
            fileItems.value = fileListUseCase.listFiles()
        }
    }
}

sealed class FileHandlerRequestState {
    data class ReadFile(val fileId: String) : FileHandlerRequestState()
    data class DownloadFile(val file: File) : FileHandlerRequestState()
    object Idle : FileHandlerRequestState()
}