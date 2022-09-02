package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.utils.toDriveFileMetadata
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val fileRepository: FileRepository
): FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return fileRepository.listFiles(pdfQuery).map { it.toDriveFileMetadata()}
    }

    override suspend fun downloadMedia(fileId: String): File {
        return fileRepository.downloadMedia(fileId)
    }

    companion object {
        private const val pdfQuery = "mimeType = 'application/pdf'"
    }

}