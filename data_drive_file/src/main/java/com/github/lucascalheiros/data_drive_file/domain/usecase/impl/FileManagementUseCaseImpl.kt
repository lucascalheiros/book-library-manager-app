package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import android.net.Uri
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.data_drive_file.domain.repository.LocalFileRepository
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase


class FileManagementUseCaseImpl(
    private val localFileRepository: LocalFileRepository
) : FileManagementUseCase {

    override suspend fun uploadFile(uri: Uri): String {
        return localFileRepository.uploadFile(uri)
    }

    override suspend fun deleteFile(id: String) {
        localFileRepository.softDelete(id)
    }

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile {
        return localFileRepository.updateFileInfo(fileId, name, tags, readProgress, totalPages)
    }

}