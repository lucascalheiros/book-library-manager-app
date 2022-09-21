package com.github.lucascalheiros.booklibrarymanager.useCase

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.io.File
import java.io.FileOutputStream

@Suppress("BlockingMethodInNonBlockingContext")
@Single
class FileCacheUseCaseImpl(
    private val context: Context
) : FileCacheUseCase {
    override suspend fun getFile(fileId: String): File? = withContext(Dispatchers.IO) {
        try {
            File(context.externalCacheDir, fileId).let {
                if (it.exists()) it else null
            }
        } catch (e: Exception) {
            Log.e(TAG, "::getFile", e)
            null
        }
    }

    override suspend fun writeFile(fileId: String, file: File): Unit = withContext(Dispatchers.IO) {
        try {
            val fileToSave = File(context.externalCacheDir, fileId)
            val fos = FileOutputStream(fileToSave)
            fos.write(file.readBytes())
            fos.close()
        } catch (e: Exception) {
            Log.e(TAG, "::writeFile", e)
        }
    }

    companion object {
        private val TAG = FileCacheUseCaseImpl::class.java.canonicalName
    }
}