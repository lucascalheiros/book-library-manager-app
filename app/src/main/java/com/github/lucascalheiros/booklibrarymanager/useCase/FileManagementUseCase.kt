package com.github.lucascalheiros.booklibrarymanager.useCase

import android.net.Uri

interface FileManagementUseCase {

    suspend fun uploadFile(uri: Uri): String
}