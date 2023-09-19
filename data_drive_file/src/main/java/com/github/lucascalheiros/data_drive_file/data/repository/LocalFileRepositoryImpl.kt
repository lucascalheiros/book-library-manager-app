package com.github.lucascalheiros.data_drive_file.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.getFileName
import com.github.lucascalheiros.common.utils.loadFileFromInputStream
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.common.utils.toBitmap
import com.github.lucascalheiros.data_drive_file.data.database.filesMetadataDatabase
import com.github.lucascalheiros.data_drive_file.data.model.LocalFileMetadata
import com.github.lucascalheiros.data_drive_file.data.model.LocalFileMetadata.Companion.tagsToString
import com.github.lucascalheiros.data_drive_file.domain.repository.LocalFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.UUID

internal class LocalFileRepositoryImpl(
    private val context: Context
) : LocalFileRepository {

    private val filesDao = context.filesMetadataDatabase().localFileMetadataDao()

    private suspend fun getFile(fileId: String): File? = withContext(Dispatchers.IO) {
        try {
            File(context.filesDir, fileId).let {
                if (it.exists()) it else null
            }
        } catch (e: Exception) {
            logError(
                listOf(LogTags.FILE_CACHE, TAG), "::getFile", e
            )
            null
        }
    }

    private suspend fun writeFile(fileId: String, file: File): File = withContext(Dispatchers.IO) {
        try {
            val fileToSave = File(context.filesDir, fileId)
            val fos = FileOutputStream(fileToSave)
            fos.write(file.readBytes())
            fos.close()
            return@withContext fileToSave
        } catch (e: Exception) {
            logError(
                listOf(LogTags.FILE_CACHE, TAG), "::writeFile", e
            )
            throw e
        }
    }

    private suspend fun saveImage(fileId: String, image: Bitmap): File =
        withContext(Dispatchers.IO) {
            try {
                val quality = 70
                val fileToSave = File(context.filesDir, "$fileId.jpg")
                val fos = FileOutputStream(fileToSave)
                image.compress(Bitmap.CompressFormat.JPEG, quality, fos)
                fos.close()
                return@withContext fileToSave
            } catch (e: Exception) {
                logError(
                    listOf(LogTags.FILE_CACHE, TAG), "::writeFile", e
                )
                throw e
            }
        }

    override suspend fun getByLocalId(id: String): BookLibFile? {
        return filesDao.findByLocalId(id)
    }

    override suspend fun getByCloudId(id: String): BookLibFile? {
        return filesDao.findByCloudId(id)
    }

    override suspend fun insert(file: BookLibFile) {
        filesDao.insertAll(
            file.run {
                LocalFileMetadata(
                    localId,
                    cloudId,
                    name,
                    createdTime,
                    modifiedTime,
                    readProgress,
                    totalPages,
                    thumbnailLink,
                    hasPendingUpdate,
                    deleted,
                    tags.tagsToString()
                )
            }
        )
    }

    override suspend fun uploadFile(uri: Uri): String {
        val fileId = UUID.randomUUID().toString()
        val contentResolver = context.contentResolver
        val input = withContext(Dispatchers.IO) { contentResolver.openInputStream(uri) }!!
        val name = getFileName(context, uri)!!
        val file = loadFileFromInputStream(context, input, name)
        val thumbnail = saveThumbnailOfPdfFile(fileId, file)
        val fileMetadata =
            LocalFileMetadata(localId = fileId, name = name, thumbnailLink = thumbnail.absolutePath)
        writeFile(fileId, file)
        filesDao.insertAll(fileMetadata)
        return fileId
    }

    private suspend fun saveThumbnailOfPdfFile(fileId: String, file: File): File {
        val fileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        return PdfRenderer(fileDescriptor).openPage(0).toBitmap().let {
            saveImage(fileId, it)
        }
    }

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile {
        val file = filesDao.findByLocalId(fileId)!!
        val newFile = file.copy(
            name = name ?: file.name,
            tagsString = (tags ?: file.tags).tagsToString(),
            readProgress = readProgress ?: file.readProgress,
            totalPages = totalPages ?: file.totalPages,
            modifiedTime = LocalDateTime.now(),
            hasPendingUpdate = true
        )
        filesDao.insertAll(newFile)
        return newFile
    }

    override suspend fun updateCloudId(localId: String, cloudId: String) {
        filesDao.updateCloudId(localId, cloudId)
    }

    override suspend fun updatePendingUpdate(localId: String, state: Boolean) {
        filesDao.updatePendingUpdate(localId, state)
    }

    override suspend fun downloadMedia(localId: String): File? {
        return getFile(localId)
    }

    override suspend fun listFiles(onlyValid: Boolean): List<BookLibFile> {
        return filesDao.getAll(onlyValid)
    }

    override fun listFilesFlow(onlyValid: Boolean): Flow<List<BookLibFile>> {
        return filesDao.getAllFlow(onlyValid)
    }

    override suspend fun softDelete(localId: String) {
        filesDao.softDelete(localId)
    }

    override suspend fun hardDelete(localId: String) {
        filesDao.hardDelete(localId)
        File(context.filesDir, localId).delete()
    }

    companion object {
        private val TAG = LocalFileRepositoryImpl::class.java.simpleName
    }
}