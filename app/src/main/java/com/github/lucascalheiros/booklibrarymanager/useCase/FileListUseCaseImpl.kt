package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.utils.toDriveFileMetadata
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val fileRepository: FileRepository,
    private val fileCacheUseCase: FileCacheUseCase
) : FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return fileRepository.listFiles(pdfQuery).map { it.toDriveFileMetadata() }
    }

    override suspend fun downloadMedia(fileId: String): File {
        return fileCacheUseCase.getFile(fileId) ?: fileRepository.downloadMedia(fileId).also {
            fileCacheUseCase.writeFile(fileId, it)
        }
    }

    companion object {
        private const val pdfQuery = "mimeType = 'application/pdf'"
    }

}