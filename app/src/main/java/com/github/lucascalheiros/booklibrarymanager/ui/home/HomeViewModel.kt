package com.github.lucascalheiros.booklibrarymanager.ui.home

import android.net.Uri
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.ui.dialogs.editFileMetadata.model.EditFileMetadataDialogInfo
import com.github.lucascalheiros.booklibrarymanager.ui.home.handlers.BookLibFileItemListener
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

    private val mLoadFilesRequestState = MutableLiveData<LoadFilesRequestState>()

    private val mFileItems = MediatorLiveData<List<BookLibFile>>()

    private val mFilteredAndSortedFileItems = MediatorLiveData<List<BookLibFile>>()

    val filteredAndSortedFileItems: LiveData<List<BookLibFile>> = mFilteredAndSortedFileItems

    val isLoadingFiles: LiveData<Boolean> = map(mLoadFilesRequestState) {
        it is LoadFilesRequestState.Loading
    }

    val tags = mFileItems.map { files ->
        files.flatMap { it.tags }.distinct().sorted()
    }
    val selectedTags = MutableLiveData(listOf<String>())

    val showFilterOptions = MutableLiveData(false)

    val fileHandlerRequestState =
        MutableLiveData<FileHandlerRequestState>(FileHandlerRequestState.Idle)

    val fileItemListener = MutableLiveData<BookLibFileItemListener>(object :
        BookLibFileItemListener {
        override fun download(item: BookLibFile) {
            downloadFile(item)
        }

        override fun read(item: BookLibFile) {
            readFile(item)
        }

        override fun edit(item: BookLibFile) {
            editFile(item)
        }

        override fun delete(item: BookLibFile) {
            deleteFile(item)
        }
    })

    private val mOpenEditFileMetadataDialog = MutableLiveData<EditFileMetadataDialogInfo>()
    val openEditFileMetadataDialog: LiveData<EditFileMetadataDialogInfo> =
        mOpenEditFileMetadataDialog

    init {
        mFileItems.addSource(mLoadFilesRequestState) {
            if (it is LoadFilesRequestState.Success) {
                mFileItems.value = it.files
            }
        }
        mFilteredAndSortedFileItems.addSource(mFileItems) {
            mediatorFilterFilesObserver()
        }
        mFilteredAndSortedFileItems.addSource(selectedTags) {
            mediatorFilterFilesObserver()
        }

        loadFiles()
    }

    private fun mediatorFilterFilesObserver() {
        val filesToFilter = mFileItems.value.orEmpty()
        val tagsFilter = selectedTags.value.orEmpty()
        mFilteredAndSortedFileItems.value = filesToFilter.filter { file ->
            tagsFilter.isEmpty() || tagsFilter.any { file.tags.contains(it) }
        }
    }

    fun readFile(file: BookLibFile) {
        viewModelScope.launch {
            file.id?.let {
                fileHandlerRequestState.value = FileHandlerRequestState.Loading
                fileHandlerRequestState.value =
                    FileHandlerRequestState.ReadFile(it, file.readProgress)
            }
        }
    }

    fun downloadFile(file: BookLibFile) {
        viewModelScope.launch {
            file.id?.let {
                fileHandlerRequestState.value = FileHandlerRequestState.Loading
                fileHandlerRequestState.value =
                    FileHandlerRequestState.DownloadFile(fileListUseCase.downloadMedia(it))
            }
        }
    }

    fun editFile(file: BookLibFile) {
        viewModelScope.launch {
            mOpenEditFileMetadataDialog.value =
                EditFileMetadataDialogInfo(file, tags.value ?: listOf())
        }
    }

    fun deleteFile(file: BookLibFile) {
        viewModelScope.launch {
            file.id?.let { fileManagementUseCase.deleteFile(it) }
        }
    }

    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            fileManagementUseCase.uploadFile(uri)
        }
    }

    fun loadFiles() {
        viewModelScope.launch {
            try {
                if (mLoadFilesRequestState.value is LoadFilesRequestState.Loading) {
                    return@launch
                }
                mLoadFilesRequestState.value =
                    LoadFilesRequestState.Success(fileListUseCase.listFiles())
            } catch (e: Exception) {
                mLoadFilesRequestState.value = LoadFilesRequestState.Failure(e)
            }
        }
    }

    fun handleFileHandlerRequestState() {
        fileHandlerRequestState.value = FileHandlerRequestState.Idle
    }

    fun handleOpenEditFileMetadataDialogRequestState() {
        mOpenEditFileMetadataDialog.value = null
    }

}

sealed class LoadFilesRequestState {
    data class Success(val files: List<BookLibFile>) : LoadFilesRequestState()
    data class Failure(val error: Exception) : LoadFilesRequestState()
    object Loading : LoadFilesRequestState()
    object Idle : LoadFilesRequestState()
}

sealed class FileHandlerRequestState {
    data class ReadFile(val fileId: String, val page: Int) : FileHandlerRequestState()
    data class DownloadFile(val file: File) : FileHandlerRequestState()
    object Loading : FileHandlerRequestState()
    object Idle : FileHandlerRequestState()
}