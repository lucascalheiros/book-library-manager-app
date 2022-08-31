package com.github.lucascalheiros.booklibrarymanager.ui.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.FileListItemListener
import com.github.lucascalheiros.booklibrarymanager.useCase.FileListUseCase
import com.github.lucascalheiros.booklibrarymanager.useCase.FileManagementUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.io.File

@KoinViewModel
class HomeViewModel(
    private val fileListUseCase: FileListUseCase,
    private val fileManagementUseCase: FileManagementUseCase
) : ViewModel() {

    val fileItems = MutableLiveData<List<BookLibFile>>()

    val fileHandlerRequestState =
        MutableLiveData<FileHandlerRequestState>(FileHandlerRequestState.Idle)

    val fileItemListener = MutableLiveData<FileListItemListener>(object : FileListItemListener {
        override fun download(item: BookLibFile) {
            viewModelScope.launch {
                item.id?.let {
                    fileHandlerRequestState.value = FileHandlerRequestState.Loading
                    fileHandlerRequestState.value =
                        FileHandlerRequestState.DownloadFile(fileListUseCase.getFile(it))
                }
            }
        }

        override fun read(item: BookLibFile) {
            viewModelScope.launch {
                item.id?.let {
                    fileHandlerRequestState.value = FileHandlerRequestState.Loading
                    fileHandlerRequestState.value =
                        FileHandlerRequestState.ReadFile(it)
                }
            }
        }
    })

    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            fileManagementUseCase.uploadFile(uri)
        }
    }

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
    object Loading : FileHandlerRequestState()
    object Idle : FileHandlerRequestState()
}