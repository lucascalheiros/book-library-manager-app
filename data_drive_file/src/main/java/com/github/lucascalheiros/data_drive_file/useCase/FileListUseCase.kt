package com.github.lucascalheiros.data_drive_file.useCase

import com.github.lucascalheiros.common.interfaces.BookLibFile
import java.io.File

interface FileListUseCase {

    suspend fun listFiles(): List<BookLibFile>

    suspend fun downloadMedia(fileId: String): File

}