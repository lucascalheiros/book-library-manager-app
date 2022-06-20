package com.github.lucascalheiros.booklibrarymanager.network


import com.google.api.services.drive.model.FileList
import java.io.File

interface FileRepository {

    suspend fun saveFile(
        name: String, file: File, mimeType: String,
        fileId: String? = null
    ): String

    suspend fun getFile(fileId: String, fileName: String): File

    // TODO wrap to generic
    suspend fun listFilesMetadata(): FileList
}