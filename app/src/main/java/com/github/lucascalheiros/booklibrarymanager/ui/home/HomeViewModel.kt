package com.github.lucascalheiros.booklibrarymanager.ui.home

import androidx.lifecycle.ViewModel
import com.github.lucascalheiros.booklibrarymanager.network.FileRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val fileRepository: FileRepository
): ViewModel() {



}