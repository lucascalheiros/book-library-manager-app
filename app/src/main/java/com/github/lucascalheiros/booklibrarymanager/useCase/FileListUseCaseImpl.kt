package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.data.network.DriveFileRepository
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.utils.toBookLibFile
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val driveFileRepository: DriveFileRepository,
    private val fileCacheUseCase: FileCacheUseCase
) : FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return driveFileRepository.listFiles(pdfQuery).map { it.toBookLibFile() }
    }

    override suspend fun downloadMedia(fileId: String): File {
        return fileCacheUseCase.getFile(fileId) ?: driveFileRepository.downloadMedia(fileId).also {
            fileCacheUseCase.writeFile(fileId, it)
        }
    }

    companion object {
        private const val pdfQuery = "mimeType = 'application/pdf'"
    }

}