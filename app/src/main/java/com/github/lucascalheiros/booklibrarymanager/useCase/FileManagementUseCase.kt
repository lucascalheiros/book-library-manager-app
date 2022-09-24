package com.github.lucascalheiros.booklibrarymanager.useCase

import android.net.Uri
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile

interface FileManagementUseCase {

    suspend fun uploadFile(uri: Uri): String

    suspend fun deleteFile(id: String)

    suspend fun updateFileInfo(
        fileId: String,
        name: String? = null,
        tags: List<String>? = null,
        readProgress: Int? = null,
        totalPages: Int? = null
    ): BookLibFile

}