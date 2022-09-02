package com.github.lucascalheiros.booklibrarymanager.data.network


import com.github.lucascalheiros.booklibrarymanager.model.DriveFileMetadata
import java.io.File

interface FileRepository {

    suspend fun saveFile(
        name: String,
        file: File,
        mimeType: String,
        fileId: String? = null
    ): String

    suspend fun updateFileInfo(
        fileId: String,
        name: String? = null,
        tags: List<String>? = null,
        readProgress: Int? = null,
        totalPages: Int? = null
    ): DriveFileMetadata

    suspend fun getFile(fileId: String): File

    suspend fun listFilesMetadata(query: String? = null): List<DriveFileMetadata>

    suspend fun deleteFile(fileId: String)
}