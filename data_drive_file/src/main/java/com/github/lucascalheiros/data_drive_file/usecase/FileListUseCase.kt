package com.github.lucascalheiros.data_drive_file.usecase

import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import java.io.File

interface FileListUseCase {

    suspend fun listFiles(): List<BookLibFile>

    suspend fun downloadMedia(fileId: String): File

}