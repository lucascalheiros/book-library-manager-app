package com.github.lucascalheiros.booklibrarymanager.useCase

import android.content.Context
import android.net.Uri
import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.model.DriveFileMetadata
import com.github.lucascalheiros.booklibrarymanager.utils.*
import com.github.lucascalheiros.booklibrarymanager.utils.constants.AppPropertiesKeys.READ_PROGRESS
import com.github.lucascalheiros.booklibrarymanager.utils.constants.AppPropertiesKeys.TAGS
import com.github.lucascalheiros.booklibrarymanager.utils.constants.AppPropertiesKeys.TOTAL_PAGES
import com.github.lucascalheiros.booklibrarymanager.utils.constants.DRIVE_APP_FOLDER_NAME
import com.github.lucascalheiros.booklibrarymanager.utils.constants.MimeTypeConstants
import com.google.api.client.http.FileContent
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.util.*

@Single
class FileManagementUseCaseImpl(
    private val context: Context,
    private val fileRepository: FileRepository
) : FileManagementUseCase {

    private var rootFolderId: String? = null

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun uploadFile(uri: Uri): String {
        val contentResolver = context.contentResolver
        val input = withContext(Dispatchers.IO) { contentResolver.openInputStream(uri) }!!
        val name = getFileName(context, uri)!!
        val file = loadFileFromInputStream(context, input, name)
        val metadata = File().setName(name).setParents(Collections.singletonList(getRootFolderId()))
        val mediaContent = FileContent(MimeTypeConstants.pdf, file)
        return fileRepository.createFile(metadata, mediaContent)
    }

    override suspend fun deleteFile(id: String) {
        fileRepository.deleteFile(id)
    }

    private suspend fun getRootFolderId(): String {
        return rootFolderId ?: run {
            val query =
                "mimeType = '${MimeTypeConstants.driveFolder}' and name = '$DRIVE_APP_FOLDER_NAME'"
            fileRepository.listFiles(query).firstOrNull()?.id ?: run {
                val fileMetadata = File().apply {
                    name = DRIVE_APP_FOLDER_NAME
                    mimeType = MimeTypeConstants.driveFolder
                }
                fileRepository.createFile(fileMetadata)
            }
        }.also { rootFolderId = it }
    }

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): DriveFileMetadata {
        val file: File = fileRepository.getFile(fileId)

        val newFile = File()
        newFile.appProperties = file.appProperties
        newFile.name = file.name

        newFile.apply {
            appProperties = appProperties ?: mutableMapOf()
            tags?.let { appProperties[TAGS] = it.joinToString(",") }
            readProgress?.let { appProperties[READ_PROGRESS] = "$it" }
            totalPages?.let { appProperties[TOTAL_PAGES] = "$it" }
            name?.let { this.name = it }
        }

        return fileRepository.updateFileInfo(fileId, newFile).toDriveFileMetadata()
    }

}