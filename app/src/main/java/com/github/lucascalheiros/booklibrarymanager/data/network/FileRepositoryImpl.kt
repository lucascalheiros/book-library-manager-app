package com.github.lucascalheiros.booklibrarymanager.data.network

import android.content.Context
import com.github.lucascalheiros.booklibrarymanager.model.DriveFileMetadata
import com.github.lucascalheiros.booklibrarymanager.utils.toDriveFileMetadata
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

    private suspend fun doCreateFile(
        fileMetadata: File,
        mediaContent: FileContent?
    ): String = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val driveFile = driveService().files().let { files ->
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
                        .execute()
                    continuation.resume(driveFile.id)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

    override suspend fun createFile(fileMetadata: File): String {
        return doCreateFile(
            fileMetadata,
            null
        )
    }

    override suspend fun createFile(fileMetadata: File, mediaContent: FileContent): String {
        return doCreateFile(
            fileMetadata,
            mediaContent
        )
    }

    override suspend fun updateFileInfo(
        fileMetadata: File
    ): File = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val driveFile = driveService().files().update(
                        fileMetadata.id,
                        fileMetadata
                    )
                        .setFields("*")
                        .execute()
                    continuation.resume(driveFile)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

    override suspend fun downloadMedia(fileId: String): java.io.File =
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

    override suspend fun getFile(fileId: String): File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val driveFile = driveService().files()[fileId].setFields("*").execute()
                        continuation.resume(driveFile)
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    override suspend fun listFiles(query: String?): List<File> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val googleDriveFileList =
                        driveService().files().list().setQ(query).setFields("*")
                            .execute().files
                    continuation.resume(googleDriveFileList)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

    override suspend fun deleteFile(fileId: String): Unit = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    driveService().files().delete(fileId).execute()
                    continuation.resume(Unit)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

}