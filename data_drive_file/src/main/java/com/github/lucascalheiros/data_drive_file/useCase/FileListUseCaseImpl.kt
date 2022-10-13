package com.github.lucascalheiros.data_drive_file.useCase

import com.github.lucascalheiros.commom.interfaces.BookLibFile
import com.github.lucascalheiros.commom.utils.constants.MimeTypeConstants
import com.github.lucascalheiros.data_drive_file.utils.DriveQueryBuilder
import com.github.lucascalheiros.data_drive_file.utils.toBookLibFile
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileListUseCaseImpl(
    private val driveFileRepository: com.github.lucascalheiros.data_drive_file.data.network.DriveFileRepository,
    private val fileCacheUseCase: FileCacheUseCase
) : FileListUseCase {

    override suspend fun listFiles(): List<BookLibFile> {
        return driveFileRepository.listFiles(
            DriveQueryBuilder().mimeTypeEquals(MimeTypeConstants.pdf).build()
        ).map { it.toBookLibFile() }
    }

    override suspend fun downloadMedia(fileId: String): File {
        return fileCacheUseCase.getFile(fileId) ?: driveFileRepository.downloadMedia(fileId).also {
            fileCacheUseCase.writeFile(fileId, it)
        }
    }
}