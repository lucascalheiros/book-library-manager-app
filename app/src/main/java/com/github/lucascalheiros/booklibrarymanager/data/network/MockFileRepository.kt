package com.github.lucascalheiros.booklibrarymanager.data.network

import android.content.Context
import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata
import com.github.lucascalheiros.booklibrarymanager.utils.loadFileFromAsset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//@Single
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

    override suspend fun listFilesMetadata(query: String?): List<FileMetadata> = withContext(Dispatchers.IO) {
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