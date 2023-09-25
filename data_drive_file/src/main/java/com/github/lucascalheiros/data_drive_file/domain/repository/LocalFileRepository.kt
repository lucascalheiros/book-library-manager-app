package com.github.lucascalheiros.data_drive_file.domain.repository

import android.net.Uri
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import kotlinx.coroutines.flow.Flow
import java.io.File

interface LocalFileRepository {
    suspend fun listFiles(onlyValid: Boolean = true): List<BookLibFile>
    fun listFilesFlow(onlyValid: Boolean = true): Flow<List<BookLibFile>>
    suspend fun getByLocalId(id: String): BookLibFile?
    suspend fun getByCloudId(id: String): BookLibFile?
    suspend fun insert(file: BookLibFile)
    suspend fun uploadFile(uri: Uri): String
    suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile
    suspend fun updateCloudId(localId: String, cloudId: String)
    suspend fun updatePendingUpdate(localId: String, state: Boolean)
    suspend fun downloadMedia(localId: String): File?
    suspend fun softDelete(localId: String)
    suspend fun hardDelete(localId: String)
}