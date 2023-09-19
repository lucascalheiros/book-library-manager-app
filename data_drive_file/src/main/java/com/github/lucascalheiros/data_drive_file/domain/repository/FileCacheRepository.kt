package com.github.lucascalheiros.data_drive_file.domain.repository

import java.io.File

interface FileCacheRepository {
    suspend fun getFile(fileId: String): File?
    suspend fun writeFile(fileId: String, file: File)
}