package com.github.lucascalheiros.booklibrarymanager.ui.dialogs.editFileMetadata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.ui.dialogs.editFileMetadata.handlers.EditFileMetadataDialogHandler
import com.github.lucascalheiros.booklibrarymanager.ui.dialogs.editFileMetadata.model.EditFileMetadataDialogInfo
import com.github.lucascalheiros.booklibrarymanager.useCase.FileManagementUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EditFileMetadataDialogViewModel(
    private val fileManagementUseCase: FileManagementUseCase
) : ViewModel(), EditFileMetadataDialogHandler {

    private var fileId: String? = null
    override val name = MutableLiveData<String>()
    override val tagOptions = MutableLiveData<List<String>>()
    override val selectedTags = MutableLiveData<List<String>>()
    override val tagToAdd = MutableLiveData<String>()

    private val mEditFileMetadataDialogState = MutableLiveData<EditFileMetadataDialogState>()
    val editFileMetadataDialogState: LiveData<EditFileMetadataDialogState> =
        mEditFileMetadataDialogState

    override fun addTag() {
        tagToAdd.value?.let { newTag ->
            tagOptions.value?.let {
                if (!it.contains(newTag)) {
                    tagOptions.value = it.toMutableList().apply { add(newTag) }
                }
            }

            selectedTags.value?.let {
                if (!it.contains(newTag)) {
                    selectedTags.value = it.toMutableList().apply { add(newTag) }
                }
            }
            tagToAdd.value = ""
        }
    }

    override fun save() {
        viewModelScope.launch {
            if (mEditFileMetadataDialogState.value is EditFileMetadataDialogState.Loading) {
                return@launch
            }
            mEditFileMetadataDialogState.value = EditFileMetadataDialogState.Loading
            mEditFileMetadataDialogState.value = try {
                fileManagementUseCase.updateFileInfo(
                    fileId = fileId!!,
                    name = name.value,
                    tags = selectedTags.value
                )
                EditFileMetadataDialogState.Success
            } catch (e: Exception) {
                EditFileMetadataDialogState.Failure
            }
        }
    }

    override fun cancel() {
        if (mEditFileMetadataDialogState.value is EditFileMetadataDialogState.Loading) {
            return
        } else {
            mEditFileMetadataDialogState.value = EditFileMetadataDialogState.Cancel
        }
    }

    fun handleEditFileMetadataState() {
        mEditFileMetadataDialogState.value = EditFileMetadataDialogState.Idle
    }

    fun setData(
        info: EditFileMetadataDialogInfo
    ) {
        fileId = fileId ?: info.file.id
        name.value = name.value ?: info.file.name
        tagOptions.value = tagOptions.value ?: info.tagOptions
        selectedTags.value = selectedTags.value ?: info.file.tags
    }

}

sealed class EditFileMetadataDialogState {
    object Success : EditFileMetadataDialogState()
    object Failure : EditFileMetadataDialogState()
    object Cancel : EditFileMetadataDialogState()
    object Loading : EditFileMetadataDialogState()
    object Idle : EditFileMetadataDialogState()
}


