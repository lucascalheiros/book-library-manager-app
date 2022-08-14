package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.model.FileListItem
import java.io.File

interface FileListUseCase {

    suspend fun listFiles(): List<FileListItem>

    suspend fun getFile(fileId: String): File
}