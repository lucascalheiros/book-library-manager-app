package com.github.lucascalheiros.booklibrarymanager.useCase


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
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
class GoogleDriveUseCase constructor(
    private val context: Context
) {

    fun driveService(): Drive {
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


    suspend fun saveFile(
        name: String,
        path: String,
        mimeType: String,
        fileId: String? = null
    ): File {
        val metadata = File().setName(name)

        val filePath = java.io.File(path)
        val mediaContent = FileContent(mimeType, filePath)

        return saveFile(metadata, mediaContent)
    }

    suspend fun saveFile(metadata: File, mediaContent: FileContent, fileId: String? = null): File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val driveFile = if (fileId == null) {
                            driveService().files().create(metadata, mediaContent)
                                .setFields("id")
                                .execute()
                        } else {
                            driveService().files().update(fileId, metadata, mediaContent).execute()
                        }

                        continuation.resume(driveFile)
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    suspend fun getFile(fileId: String, fileName: String): java.io.File =
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

    suspend fun listFiles(): FileList = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val result = driveService().files().list().execute()
                    continuation.resume(result)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

}