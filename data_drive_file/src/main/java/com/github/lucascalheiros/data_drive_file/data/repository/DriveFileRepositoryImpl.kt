package com.github.lucascalheiros.data_drive_file.data.repository

import android.content.Context
import android.net.Uri
import com.github.lucascalheiros.common.BuildConfig
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import com.github.lucascalheiros.data_drive_file.data.constants.AppPropertiesKeys
import com.github.lucascalheiros.common.utils.constants.MimeTypeConstants
import com.github.lucascalheiros.common.utils.getFileName
import com.github.lucascalheiros.common.utils.loadFileFromInputStream
import com.github.lucascalheiros.data_drive_file.data.model.LocalFileMetadata.Companion.tagsToString
import com.github.lucascalheiros.data_drive_file.data.model.adapter.toBookLibFile
import com.github.lucascalheiros.data_drive_file.data.utils.DriveQueryBuilder
import com.github.lucascalheiros.data_drive_file.domain.repository.DriveFileRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.ZoneId
import java.util.Collections

internal class DriveFileRepositoryImpl(
    private val context: Context
) : DriveFileRepository {

    private fun driveService(): Drive {
        val credential: GoogleAccountCredential = GoogleAccountCredential.usingOAuth2(
            context, Collections.singleton(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = lastSignedInAccount?.account!! // TODO create specific exception
        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        ).setApplicationName(BuildConfig.LIBRARY_PACKAGE_NAME)
            .build()
    }

    private val lastSignedInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)

    private suspend fun doCreateFile(
        fileMetadata: File,
        mediaContent: FileContent?
    ): String = withContext(Dispatchers.IO) {
        driveService().files().let { files ->
            mediaContent?.let {
                files.create(
                    fileMetadata,
                    it
                )
            } ?: run {
                files.create(
                    fileMetadata
                )
            }
        }
            .setFields("*")
            .execute().id
    }

    private suspend fun createFile(fileMetadata: File): String {
        return doCreateFile(
            fileMetadata,
            null
        )
    }

    private suspend fun createFile(fileMetadata: File, mediaContent: FileContent): String {
        return doCreateFile(
            fileMetadata,
            mediaContent
        )
    }

    private suspend fun updateFileInfo(
        fileId: String,
        fileMetadata: File
    ): File = withContext(Dispatchers.IO) {
        driveService().files().update(
            fileId,
            fileMetadata
        )
            .setFields("*")
            .execute()
    }

    override suspend fun syncFileInfo(bookLibFile: BookLibFile) {
        val cloudId = bookLibFile.cloudId!!
        val file: File = getFile(cloudId)
        val newFile = File()
        newFile.appProperties = file.appProperties
        newFile.name = file.name
        newFile.apply {
            appProperties = appProperties ?: mutableMapOf()
            appProperties[AppPropertiesKeys.TAGS] = bookLibFile.tags.tagsToString()
            appProperties[AppPropertiesKeys.READ_PROGRESS] = bookLibFile.readProgress.toString()
            appProperties[AppPropertiesKeys.TOTAL_PAGES] = bookLibFile.totalPages.toString()
            appProperties[AppPropertiesKeys.UPDATE_TIME] = bookLibFile.modifiedTime
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli().toString()
            name = bookLibFile.name
        }
        updateFileInfo(cloudId, newFile)
    }

    override suspend fun downloadMedia(fileId: String): java.io.File = withContext(Dispatchers.IO) {
        val tempFileName = System.currentTimeMillis().toString()
        val outFile = java.io.File(context.cacheDir, tempFileName)
        val outStream: OutputStream = FileOutputStream(outFile)
        driveService().files()[fileId]
            .executeMediaAndDownloadTo(outStream)
        outStream.close()
        outFile
    }

    private suspend fun getFile(fileId: String): File = withContext(Dispatchers.IO) {
        driveService().files()[fileId].setFields("*").execute()
    }

    private suspend fun listFiles(query: String?): List<File> = withContext(Dispatchers.IO) {
        driveService().files().list().setQ(query).setFields("*")
            .execute().files
    }

    override suspend fun listFiles(): List<BookLibFile> {
        return listFiles(
            DriveQueryBuilder().mimeTypeEquals(MimeTypeConstants.pdf).build()
        ).map { it.toBookLibFile() }
    }

    override suspend fun deleteFile(fileId: String): Unit = withContext(Dispatchers.IO) {
        driveService().files().delete(fileId).execute()
    }

    private suspend fun getRootFolderId(): String {
        val query = DriveQueryBuilder()
            .mimeTypeEquals(MimeTypeConstants.driveFolder)
            .nameEquals(BuildConfig.LIBRARY_PACKAGE_NAME)
            .build()
        return listFiles(query).firstOrNull()?.id ?: run {
            val fileMetadata = File().apply {
                name = BuildConfig.LIBRARY_PACKAGE_NAME
                mimeType = MimeTypeConstants.driveFolder
            }
            createFile(fileMetadata)
        }
    }

    override suspend fun uploadFile(uri: Uri): String {
        val contentResolver = context.contentResolver
        val input = withContext(Dispatchers.IO) { contentResolver.openInputStream(uri) }!!
        val name = getFileName(context, uri)!!
        val file = loadFileFromInputStream(context, input, name)
        val metadata = File().setName(name).setParents(Collections.singletonList(getRootFolderId()))
        val mediaContent = FileContent(MimeTypeConstants.pdf, file)
        return createFile(metadata, mediaContent)
    }

    override fun isDriveAvailable(): Boolean {
        return lastSignedInAccount != null
    }
}
