package com.github.lucascalheiros.data_drive_file.data.repository

import android.content.Context
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_drive_file.domain.repository.FileCacheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

internal class FileCacheRepositoryImpl(
    private val context: Context
) : FileCacheRepository {
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
        private val TAG = FileCacheRepositoryImpl::class.java.simpleName
    }
}