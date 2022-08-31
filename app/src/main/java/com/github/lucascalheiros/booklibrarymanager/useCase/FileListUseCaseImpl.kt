package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val fileRepository: FileRepository
): FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return fileRepository.listFilesMetadata(pdfQuery)
    }

    override suspend fun getFile(fileId: String): File {
        return fileRepository.getFile(fileId)
    }

    companion object {
        private const val pdfQuery = "mimeType = 'application/pdf'"
    }
}