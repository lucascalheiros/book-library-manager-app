package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.data_drive_file.domain.network.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileCacheUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileListUseCase
import java.io.File

class FileListUseCaseImpl(
    private val driveFileRepository: DriveFileRepository,
    private val fileCacheUseCase: FileCacheUseCase
) : FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return driveFileRepository.listPdfFiles()
    }

    override suspend fun downloadMedia(fileId: String): File {
        return fileCacheUseCase.getFile(fileId) ?: driveFileRepository.downloadMedia(fileId).also {
            fileCacheUseCase.writeFile(fileId, it)
        }
    }
}