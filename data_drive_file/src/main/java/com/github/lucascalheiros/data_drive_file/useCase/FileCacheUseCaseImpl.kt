package com.github.lucascalheiros.data_drive_file.useCase

import android.content.Context
import com.github.lucascalheiros.commom.utils.constants.LogTags
import com.github.lucascalheiros.commom.utils.logError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Suppress("BlockingMethodInNonBlockingContext")

class FileCacheUseCaseImpl(
    private val context: Context
) : FileCacheUseCase {
    override suspend fun getFile(fileId: String): File? = withContext(Dispatchers.IO) {
        try {
            File(context.externalCacheDir, fileId).let {
                if (it.exists()) it else null
            }
        } catch (e: Exception) {
            logError(
                listOf(LogTags.FILE_CACHE, TAG), "::getFile", e
            )
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
            logError(
                listOf(LogTags.FILE_CACHE, TAG), "::writeFile", e
            )
        }
    }

    companion object {
        private val TAG = FileCacheUseCaseImpl::class.java.canonicalName
    }
}