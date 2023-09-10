package com.github.lucascalheiros.data_drive_file.domain.repository

import android.net.Uri
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import java.io.File

interface DriveFileRepository {
    suspend fun uploadFile(uri: Uri): String
    suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile
    suspend fun downloadMedia(fileId: String): File
    suspend fun listPdfFiles(): List<BookLibFile>
    suspend fun deleteFile(fileId: String)
}