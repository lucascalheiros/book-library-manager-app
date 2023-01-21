package com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper

import com.github.lucascalheiros.booklibrarymanager.data.network.DriveFileRepository
import com.github.lucascalheiros.booklibrarymanager.utils.constants.DRIVE_APP_FOLDER_NAME
import com.github.lucascalheiros.booklibrarymanager.utils.constants.MimeTypeConstants
import com.github.lucascalheiros.booklibrarymanager.utils.driveUtils.DriveQueryBuilder
import com.google.api.client.http.FileContent
import com.google.api.services.drive.model.File
import java.util.*

class CreateDriveWrapperImpl(
    private val driveFileRepository: DriveFileRepository
): CreateDriveWrapper {

    override suspend fun createFile(filename: String, fileContent: java.io.File): String {
        val metadata = File().setName(filename).setParents(Collections.singletonList(getRootFolderId()))
        val mediaContent = FileContent(MimeTypeConstants.pdf, fileContent)
        return driveFileRepository.createFile(metadata, mediaContent)    }

    private var rootFolderId: String? = null

    private suspend fun getRootFolderId(): String {
        return rootFolderId ?: run {
            val query = DriveQueryBuilder()
                .mimeTypeEquals(MimeTypeConstants.driveFolder)
                .nameEquals(DRIVE_APP_FOLDER_NAME)
                .build()
            driveFileRepository.listFiles(query).firstOrNull()?.id ?: run {
                val fileMetadata = File().apply {
                    name = DRIVE_APP_FOLDER_NAME
                    mimeType = MimeTypeConstants.driveFolder
                }
                driveFileRepository.createFile(fileMetadata)
            }
        }.also { rootFolderId = it }
    }

}