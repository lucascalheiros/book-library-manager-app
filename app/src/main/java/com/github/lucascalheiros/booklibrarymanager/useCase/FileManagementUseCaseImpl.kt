package com.github.lucascalheiros.booklibrarymanager.useCase

import android.content.Context
import android.net.Uri
import com.github.lucascalheiros.booklibrarymanager.data.network.BookLibFileRepository
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.utils.getFileName
import com.github.lucascalheiros.booklibrarymanager.utils.loadFileFromInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class FileManagementUseCaseImpl(
    private val context: Context,
    private val bookLibFileRepository: BookLibFileRepository
) : FileManagementUseCase {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun uploadFile(uri: Uri): String {
        val contentResolver = context.contentResolver
        val input = withContext(Dispatchers.IO) { contentResolver.openInputStream(uri) }!!
        val name = getFileName(context, uri)!!
        val file = loadFileFromInputStream(context, input, name)
        return bookLibFileRepository.createFile(name, file)
    }

    override suspend fun deleteFile(id: String) {
        bookLibFileRepository.deleteFile(id)
    }

    override suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile {
        return bookLibFileRepository.updateFileInfo(fileId, name, tags, readProgress, totalPages)
    }

}