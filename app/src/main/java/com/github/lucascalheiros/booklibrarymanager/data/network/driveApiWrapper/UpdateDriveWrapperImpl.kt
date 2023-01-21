package com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper

import com.github.lucascalheiros.booklibrarymanager.data.network.DriveFileRepository
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.utils.constants.AppPropertiesKeys
import com.github.lucascalheiros.booklibrarymanager.utils.driveUtils.toBookLibFile
import com.google.api.services.drive.model.File

class UpdateDriveWrapperImpl(
    private val driveFileRepository: DriveFileRepository
): UpdateDriveWrapper {

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile {
        val file: File = driveFileRepository.getFile(fileId)

        val newFile = File()
        newFile.appProperties = file.appProperties
        newFile.name = file.name

        newFile.apply {
            appProperties = appProperties ?: mutableMapOf()
            tags?.let { appProperties[AppPropertiesKeys.TAGS] = it.joinToString(",") }
            readProgress?.let { appProperties[AppPropertiesKeys.READ_PROGRESS] = "$it" }
            totalPages?.let { appProperties[AppPropertiesKeys.TOTAL_PAGES] = "$it" }
            name?.let { this.name = it }
        }

        return driveFileRepository.updateFileInfo(fileId, newFile).toBookLibFile()
    }

}