package com.github.lucascalheiros.data_drive_file.domain.usecase

import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FetchFilesUseCase {
    fun listFilesFlow(): Flow<List<BookLibFile>>
    suspend fun listFiles(): List<BookLibFile>
    suspend fun downloadMedia(fileId: String): File
    suspend fun fetchFiles()
}