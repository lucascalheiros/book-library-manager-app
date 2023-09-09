package com.github.lucascalheiros.feature_home.presentation.home

import android.net.Uri
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileListUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase
import com.github.lucascalheiros.feature_home.presentation.editFileMetadata.model.EditFileMetadataDialogInfo
import com.github.lucascalheiros.feature_home.presentation.home.handlers.BookLibFileItemListener
import kotlinx.coroutines.launch
import java.io.File


class HomeViewModel(
    private val fileListUseCase: FileListUseCase,
    private val fileManagementUseCase: FileManagementUseCase
) : ViewModel() {

    private val mFileItemListener = object :
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
    }

    private val mLoadFilesRequestState = MutableLiveData<LoadFilesRequestState>()

    private val mFileItems = MediatorLiveData<List<BookLibFile>>()

    private val mFilteredAndSortedFileItems = MediatorLiveData<List<BookLibFile>>()

    private val mFileHandlerRequestState =
        MutableLiveData<FileHandlerRequestState>(FileHandlerRequestState.Idle)

    private val mOpenEditFileMetadataDialog = MutableLiveData<EditFileMetadataDialogInfo?>()

    val searchText = MutableLiveData("")

    val selectedTags = MutableLiveData(listOf<String>())

    val showFilterOptions = MutableLiveData(false)

    val isLoadingFiles: LiveData<Boolean> = map(mLoadFilesRequestState) {
        it is LoadFilesRequestState.Loading
    }.distinctUntilChanged()

    val tags: LiveData<List<String>> = mFileItems.map { files ->
        files.flatMap { it.tags }.distinct().sortedBy { it.uppercase() }
    }

    val fileItemListener: LiveData<BookLibFileItemListener> = MutableLiveData(mFileItemListener)

    val filteredAndSortedFileItems: LiveData<List<BookLibFile>> = mFilteredAndSortedFileItems

    val fileHandlerRequestState: LiveData<FileHandlerRequestState> = mFileHandlerRequestState

    val openEditFileMetadataDialog: LiveData<EditFileMetadataDialogInfo?> =
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
        mFilteredAndSortedFileItems.addSource(searchText) {
            mediatorFilterFilesObserver()
        }
    }

    private fun mediatorFilterFilesObserver() {
        val filesToFilter = mFileItems.value.orEmpty()
        val tagsFilter = selectedTags.value.orEmpty()
        mFilteredAndSortedFileItems.value = filesToFilter.filter { file ->
            (tagsFilter.isEmpty() || tagsFilter.any { file.tags.contains(it) }) && file.name.contains(
                searchText.value.orEmpty(),
                true
            )
        }.sortedByDescending { it.modifiedTime }
    }

    fun readFile(file: BookLibFile) {
        viewModelScope.launch {
            file.id?.let {
                if (mFileHandlerRequestState.value is FileHandlerRequestState.Loading) {
                    return@launch
                }
                mFileHandlerRequestState.value = FileHandlerRequestState.Loading(file)
                mFileHandlerRequestState.value =
                    FileHandlerRequestState.ReadFile(it, file.readProgress)
            }
        }
    }

    fun downloadFile(file: BookLibFile) {
        viewModelScope.launch {
            try {
                if (mFileHandlerRequestState.value is FileHandlerRequestState.Loading) {
                    return@launch
                }
                mFileHandlerRequestState.value = FileHandlerRequestState.Loading(file)
                mFileHandlerRequestState.value =
                    FileHandlerRequestState.DownloadFile(fileListUseCase.downloadMedia(file.id!!))
            } catch (e: Exception) {
                // TODO add feedback
                mFileHandlerRequestState.value = FileHandlerRequestState.Failure(e)
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
            try {
                fileManagementUseCase.deleteFile(file.id!!)
                loadFiles()
            } catch (e: Exception) {
                // TODO add feedback
            }
        }
    }

    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            try {
                fileManagementUseCase.uploadFile(uri)
                loadFiles()
            } catch (e: Exception) {
                // TODO add feedback
            }
        }
    }

    fun loadFiles() {
        viewModelScope.launch {
            try {
                if (mLoadFilesRequestState.value is LoadFilesRequestState.Loading) {
                    return@launch
                }
                mLoadFilesRequestState.value = LoadFilesRequestState.Loading
                mLoadFilesRequestState.value =
                    LoadFilesRequestState.Success(fileListUseCase.listFiles())
            } catch (e: Exception) {
                mLoadFilesRequestState.value = LoadFilesRequestState.Failure(e)
            }
        }
    }

    fun handleFileHandlerRequestState() {
        mFileHandlerRequestState.value = FileHandlerRequestState.Idle
    }

    fun handleOpenEditFileMetadataDialogRequestState() {
        mOpenEditFileMetadataDialog.value = null
    }


    fun unselectFilterTags() {
        selectedTags.value = listOf()
    }

    fun hideFilterOptions() {
        showFilterOptions.value = false
    }

    fun shouldInterceptBackPressed(): Boolean {
        val shouldInterceptToHideFilter = showFilterOptions.value == true
        if (shouldInterceptToHideFilter) {
            hideFilterOptions()
        }
        return shouldInterceptToHideFilter
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
    data class Failure(val error: Exception) : FileHandlerRequestState()
    data class Loading(val bookLibFile: BookLibFile) : FileHandlerRequestState()
    object Idle : FileHandlerRequestState()
}