package com.github.lucascalheiros.booklibrarymanager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.useCase.GoogleDriveUseCase
import com.google.api.services.drive.model.File
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val googleDriveUseCase: GoogleDriveUseCase
): ViewModel() {

    private val mFileList = MutableLiveData<List<File>>()
    val fileList: LiveData<List<File>> = mFileList

    fun updateFileList() {
        viewModelScope.launch {
            mFileList.value = googleDriveUseCase.listFiles().files
        }
    }

}