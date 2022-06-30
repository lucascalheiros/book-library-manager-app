package com.github.lucascalheiros.booklibrarymanager.useCase

import com.github.lucascalheiros.booklibrarymanager.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.converter.FileListItemConverter
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val fileRepository: FileRepository
): FileListUseCase {

    override suspend fun listFiles(): List<FileListItem> {
        return fileRepository.listFilesMetadata().map { FileListItemConverter.from(it) }
    }

    override suspend fun getFile(fileId: String): File {
        return fileRepository.getFile(fileId)
    }
}