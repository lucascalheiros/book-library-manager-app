package com.github.lucascalheiros.booklibrarymanager.useCase

import android.content.Context
import android.net.Uri
import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.utils.getFileName
import com.github.lucascalheiros.booklibrarymanager.utils.loadFileFromInputStream
import org.koin.core.annotation.Single

@Single
class FileManagementUseCaseImpl(
    private val context: Context,
    private val fileRepository: FileRepository
) : FileManagementUseCase {

    override suspend fun uploadFile(uri: Uri): String {
        val contentResolver = context.contentResolver
        val input = contentResolver.openInputStream(uri)!!
        val name = getFileName(context, uri)!!
        val file = loadFileFromInputStream(context, input, name)
        return fileRepository.saveFile(name, file, "application/pdf")
    }

    override suspend fun deleteFile(id: String) {
        fileRepository.deleteFile(id)
    }
}