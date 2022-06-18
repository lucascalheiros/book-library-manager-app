package com.github.lucascalheiros.booklibrarymanager.useCase


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
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


    suspend fun createFile(): String = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val metadata: File = File()
                        .setParents(Collections.singletonList("root"))
                        .setMimeType("text/plain")
                        .setName("Untitled file")
                    val driveFile = driveService().files().create(metadata).execute()
                    continuation.resume(driveFile.id)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }

    suspend fun saveFile(fileId: String, name: String, content: String): Unit = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            thread {
                try {
                    val metadata = File().setName(name)
                    val contentStream = ByteArrayContent.fromString("text/plain", content)
                    driveService().files().update(fileId, metadata, contentStream).execute()
                    continuation.resume(Unit)
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