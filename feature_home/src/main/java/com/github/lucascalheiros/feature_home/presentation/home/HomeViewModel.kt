package com.github.lucascalheiros.feature_home.presentation.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.utils.addSources
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import com.github.lucascalheiros.data_drive_file.domain.usecase.FetchFilesUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase
import com.github.lucascalheiros.feature_home.presentation.editFileMetadata.model.EditFileMetadataDialogInfo
import com.github.lucascalheiros.feature_home.presentation.home.model.BookLibUiModel
import kotlinx.coroutines.launch
import java.io.File


class HomeViewModel(
    private val fetchFilesUseCase: FetchFilesUseCase,
    private val fileManagementUseCase: FileManagementUseCase
) : ViewModel() {

    private val mLoadFilesRequestState = MutableLiveData<LoadFilesRequestState>()

    private val mFileItems = fetchFilesUseCase.listFilesFlow()
        .asLiveData(viewModelScope.coroutineContext)

    private val mFilteredAndSortedFileItems = MediatorLiveData<List<BookLibFile>>()

    private val mFileHandlerRequestState =
        MutableLiveData<FileHandlerRequestState>(FileHandlerRequestState.Idle)

    private val mOpenEditFileMetadataDialog = MutableLiveData<EditFileMetadataDialogInfo?>()

    val searchText = MutableLiveData("")

    val selectedTags = MutableLiveData(listOf<String>())

    val showFilterOptions = MutableLiveData(false)

    val isLoadingFiles: LiveData<Boolean> = mLoadFilesRequestState.map {
        it is LoadFilesRequestState.Loading
    }.distinctUntilChanged()

    val tags: LiveData<List<String>> = mFileItems.map { files ->
        files.flatMap { it.tags }.distinct().sortedBy { it.uppercase() }
    }

    val filteredAndSortedFileItems: LiveData<List<BookLibUiModel>> = mFilteredAndSortedFileItems.map {
        it.map {
            BookLibUiModel(
                localId = it.localId,
                cloudId = it.cloudId,
                thumbnailLink = it.thumbnailLink,
                name = it.name,
                tags = it.tags,
                readPercent = it.readPercent,
                onDownload = { downloadFile(it) },
                onRead = { readFile(it) },
                onEdit = { editFile(it) },
                onDelete = { deleteFile(it) }
            )
        }
    }

    val fileHandlerRequestState: LiveData<FileHandlerRequestState> = mFileHandlerRequestState

    val openEditFileMetadataDialog: LiveData<EditFileMetadataDialogInfo?> =
        mOpenEditFileMetadataDialog

    init {
        mFilteredAndSortedFileItems.addSources(mFileItems, selectedTags, searchText) {
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
            file.localId.let {
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
                    FileHandlerRequestState.DownloadFile(fetchFilesUseCase.downloadMedia(file.localId))
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
                fileManagementUseCase.deleteFile(file.localId)
                loadFiles()
            } catch (e: Exception) {
                logError(TAG, "::deleteFile", e)
            }
        }
    }

    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            try {
                fileManagementUseCase.uploadFile(uri)
                loadFiles()
            } catch (e: Exception) {
                logError(TAG, "::uploadFile", e)
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
                fetchFilesUseCase.fetchFiles()
                mLoadFilesRequestState.value = LoadFilesRequestState.Success
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

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}

sealed class LoadFilesRequestState {
    object Success : LoadFilesRequestState()
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