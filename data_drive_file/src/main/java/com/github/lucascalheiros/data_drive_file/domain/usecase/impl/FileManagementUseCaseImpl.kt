package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import android.net.Uri
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.data_drive_file.domain.network.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase


class FileManagementUseCaseImpl(
    private val driveFileRepository: DriveFileRepository
) : FileManagementUseCase {

    override suspend fun uploadFile(uri: Uri): String {
        return driveFileRepository.uploadFile(uri)
    }

    override suspend fun deleteFile(id: String) {
        driveFileRepository.deleteFile(id)
    }

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile {
        return driveFileRepository.updateFileInfo(fileId, name, tags, readProgress, totalPages)
    }
}