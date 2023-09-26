package com.github.lucascalheiros.data_drive_file.domain.repository

import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import java.io.File

interface DriveFileRepository {
    suspend fun uploadFile(file: File, bookLibFile: BookLibFile): String
    suspend fun syncFileInfo(bookLibFile: BookLibFile)
    suspend fun downloadMedia(fileId: String): File
    suspend fun listFiles(): List<BookLibFile>
    suspend fun deleteFile(fileId: String)
    fun isDriveAvailable(): Boolean
}