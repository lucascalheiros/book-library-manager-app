package com.github.lucascalheiros.booklibrarymanager.data.network


import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata
import java.io.File

interface FileRepository {

    suspend fun saveFile(
        name: String,
        file: File,
        mimeType: String,
        fileId: String? = null
    ): String

    suspend fun getFile(fileId: String): File

    suspend fun listFilesMetadata(query: String? = null): List<FileMetadata>
}