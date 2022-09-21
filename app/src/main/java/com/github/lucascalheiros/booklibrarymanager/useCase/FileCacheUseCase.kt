package com.github.lucascalheiros.booklibrarymanager.useCase

import java.io.File

interface FileCacheUseCase {
    suspend fun getFile(fileId: String): File?
    suspend fun writeFile(fileId: String, file: File)
}