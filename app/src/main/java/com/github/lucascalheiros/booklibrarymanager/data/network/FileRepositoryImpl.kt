package com.github.lucascalheiros.booklibrarymanager.data.network

import android.content.Context
import com.github.lucascalheiros.booklibrarymanager.model.FileDriveMetadata
import com.github.lucascalheiros.booklibrarymanager.model.converter.FileMetadataConverter
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Single
class FileRepositoryImpl(
    private val context: Context
) : FileRepository {

    private fun driveService(): Drive {
        val credential: GoogleAccountCredential = GoogleAccountCredential.usingOAuth2(
            context, Collections.singleton(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = lastSignedInAccount?.account
        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        ).setApplicationName("Booklib Manager")
            .build()
    }

    private val lastSignedInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)

    private suspend fun createFolder(folderName: String): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val query =
                            "mimeType = 'application/vnd.google-apps.folder' and name = '${folderName}'"
                        val driveFile: File = runBlocking { listFiles(query) }.let {
                            if (it.isEmpty()) {
                                val fileMetadata = File()
                                fileMetadata.name = folderName
                                fileMetadata.mimeType = "application/vnd.google-apps.folder"
                                driveService().files().create(fileMetadata)
                                    .setFields("*")
                                    .execute()
                            } else {
                                it.first()
                            }
                        }

                        continuation.resume(driveFile.id)
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    private suspend fun createDefaultAppFolder(): String {
        return createFolder(defaultAppFolderName)
    }

    override suspend fun saveFile(
        name: String,
        file: java.io.File,
        mimeType: String,
        fileId: String?
    ): String {
        val metadata = File().setName(name)

        val mediaContent = FileContent(mimeType, file)

        return saveFile(metadata, mediaContent, fileId).id
    }

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): FileDriveMetadata = withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val file: File = driveService().files().get(fileId).execute()

                        val newFile = File()
                        newFile.appProperties = file.appProperties
                        newFile.name = file.name
                        newFile.createdTime = file.createdTime

                        newFile.apply {
                            appProperties = appProperties ?: mutableMapOf()
                            tags?.let { appProperties[TAGS] = it.joinToString(",") }
                            readProgress?.let { appProperties[READ_PROGRESS] = "$it" }
                            totalPages?.let { appProperties[TOTAL_PAGES] = "$it" }
                            name?.let { this.name = it}
                        }

                        val driveFile = driveService().files().update(
                            fileId,
                            newFile
                        )
                            .setFields("*")
                            .execute()
                        continuation.resume(FileMetadataConverter.from(driveFile))
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }


    private suspend fun saveFile(metadata: File, mediaContent: FileContent, fileId: String?): File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val rootFolderId = runBlocking { createDefaultAppFolder() }
                        val defaultFolder = Collections.singletonList(rootFolderId)
                        val driveFile = if (fileId == null) {
                            driveService().files().create(
                                metadata.setParents(defaultFolder),
                                mediaContent
                            )
                                .setFields("*")
                                .execute()
                        } else {
                            driveService().files().update(
                                fileId,
                                metadata.setParents(defaultFolder),
                                mediaContent
                            )
                                .setFields("*")
                                .execute()
                        }
                        continuation.resume(driveFile)
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    override suspend fun getFile(fileId: String): java.io.File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val tempFileName = System.currentTimeMillis().toString()
                        val outFile = java.io.File(context.cacheDir, tempFileName)
                        val outStream: OutputStream = FileOutputStream(outFile)
                        driveService().files()[fileId]
                            .executeMediaAndDownloadTo(outStream)
                        outStream.close()
                        continuation.resume(outFile)
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    private suspend fun listFiles(query: String?): List<File> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val googleDriveFileList = driveService().files().list().setQ(query).setFields("*")
                        .execute().files
                    continuation.resume(googleDriveFileList)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

    override suspend fun listFilesMetadata(query: String?): List<FileDriveMetadata> {
        return listFiles(query).map { FileMetadataConverter.from(it) }
    }

    companion object {
        private const val defaultAppFolderName = "Booklib Manager"
        private const val TAGS = "TAGS"
        private const val READ_PROGRESS = "READ_PROGRESS"
        private const val TOTAL_PAGES = "TOTAL_PAGES"
    }
}