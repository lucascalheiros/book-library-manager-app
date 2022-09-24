package com.github.lucascalheiros.booklibrarymanager.data.network

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


@Single
@Suppress("BlockingMethodInNonBlockingContext")
class DriveFileRepositoryImpl(
    private val context: Context
) : DriveFileRepository {

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

    override suspend fun downloadMedia(fileId: String): java.io.File = withContext(Dispatchers.IO) {
        val tempFileName = System.currentTimeMillis().toString()
        val outFile = java.io.File(context.cacheDir, tempFileName)
        val outStream: OutputStream = FileOutputStream(outFile)
        driveService().files()[fileId]
            .executeMediaAndDownloadTo(outStream)
        outStream.close()
        outFile
    }

    override suspend fun getFile(fileId: String): File = withContext(Dispatchers.IO) {
        driveService().files()[fileId].setFields("*").execute()
    }

    override suspend fun listFiles(query: String?): List<File> = withContext(Dispatchers.IO) {
        driveService().files().list().setQ(query).setFields("*")
            .execute().files
    }

    override suspend fun deleteFile(fileId: String): Unit = withContext(Dispatchers.IO) {
        driveService().files().delete(fileId).execute()
    }

}