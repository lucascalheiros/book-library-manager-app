package com.github.lucascalheiros.booklibrarymanager.network

import android.content.Context
import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata
import com.github.lucascalheiros.booklibrarymanager.model.converter.FileMetadataConverter
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItemImpl
import com.github.lucascalheiros.booklibrarymanager.utils.loadFileFromAsset
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
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Single
class MockFileRepository(
    private val context: Context
) : FileRepository {

    private val files = (0..10).map {
        FileMetadata(
            name = "name $it",
            appProperties = mapOf("TAGS" to (0..it).joinToString(",") { n -> "tag $n" }),
            id = it.toString(),
            createdTime = LocalDateTime.now(),
            modifiedTime = LocalDateTime.now()
        )
    }.toMutableList()

    override suspend fun saveFile(
        name: String,
        file: java.io.File,
        mimeType: String,
        fileId: String?
    ): String {
        return ""
    }

    override suspend fun getFile(fileId: String): java.io.File =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                thread {
                    try {
                        continuation.resume(loadFileFromAsset(context, "clean_arch.pdf"))
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
                    continuation.resume(files)
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        }
    }
}