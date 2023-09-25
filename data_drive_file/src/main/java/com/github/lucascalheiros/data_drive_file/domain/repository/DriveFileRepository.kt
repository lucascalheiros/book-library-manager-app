package com.github.lucascalheiros.data_drive_file.domain.repository

import android.net.Uri
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import java.io.File

interface DriveFileRepository {
    suspend fun uploadFile(uri: Uri): String
    suspend fun syncFileInfo(bookLibFile: BookLibFile)
    suspend fun downloadMedia(fileId: String): File
    suspend fun listFiles(): List<BookLibFile>
    suspend fun deleteFile(fileId: String)
    fun isDriveAvailable(): Boolean
}