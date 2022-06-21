package com.github.lucascalheiros.booklibrarymanager.network

import android.content.Context
import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata
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
import org.koin.core.annotation.Single
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Single
class GoogleDriveFileRepositoryImpl(
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


    private suspend fun saveFile(metadata: File, mediaContent: FileContent, fileId: String?): File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val defaultFolder = Collections.singletonList("Booklib Manager");
                        val driveFile = if (fileId == null) {
                            driveService().files().create(
                                metadata.setParents(defaultFolder),
                                mediaContent
                            )
                                .setFields("id")
                                .execute()
                        } else {
                            driveService().files().update(
                                fileId,
                                metadata.setParents(defaultFolder),
                                mediaContent
                            ).execute()
                        }
                        continuation.resume(driveFile)
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    override suspend fun getFile(fileId: String, fileName: String): java.io.File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val outFile = java.io.File(context.cacheDir, fileName)

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

    override suspend fun listFilesMetadata(): List<FileMetadata> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val googleDriveFileList = driveService().files().list().execute().files
                    val metadataList = googleDriveFileList.map { FileMetadata.fromGoogleDrive(it) }
                    continuation.resume(metadataList)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }
}