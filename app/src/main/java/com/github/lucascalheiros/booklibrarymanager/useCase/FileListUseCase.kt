package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import java.io.File

interface FileListUseCase {

    suspend fun listFiles(): List<BookLibFile>

    suspend fun downloadMedia(fileId: String): File

}