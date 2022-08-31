package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile
import java.io.File

interface FileListUseCase {

    suspend fun listFiles(): List<BookLibFile>

    suspend fun getFile(fileId: String): File
}