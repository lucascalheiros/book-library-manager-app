package com.github.lucascalheiros.data_drive_file.domain.usecase

import java.io.File

interface FileCacheUseCase {
    suspend fun getFile(fileId: String): File?
    suspend fun writeFile(fileId: String, file: File)
}