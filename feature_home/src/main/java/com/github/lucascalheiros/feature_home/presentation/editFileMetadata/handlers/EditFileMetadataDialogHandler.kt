package com.github.lucascalheiros.feature_home.presentation.editFileMetadata.handlers

import androidx.lifecycle.MutableLiveData

interface EditFileMetadataDialogHandler {
    val name: MutableLiveData<String>
    val tagOptions: MutableLiveData<List<String>>
    val selectedTags: MutableLiveData<List<String>>
    val tagToAdd: MutableLiveData<String>
    fun addTag()
    fun save()
    fun cancel()
}