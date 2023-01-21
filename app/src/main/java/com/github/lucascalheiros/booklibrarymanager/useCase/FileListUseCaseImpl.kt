package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.data.network.BookLibFileRepository
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val bookLibFileRepository: BookLibFileRepository,
    private val fileCacheUseCase: FileCacheUseCase
) : FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return bookLibFileRepository.listFiles()
    }

    override suspend fun downloadMedia(fileId: String): File {
        return fileCacheUseCase.getFile(fileId) ?: bookLibFileRepository.downloadMedia(fileId).also {
            fileCacheUseCase.writeFile(fileId, it)
        }
    }
}