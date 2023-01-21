package com.github.lucascalheiros.booklibrarymanager.data.network


import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import java.io.File

interface BookLibFileRepository {

    suspend fun createFile(
        filename: String,
        fileContent: File
    ): String

    suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile

    suspend fun downloadMedia(fileId: String): File

    suspend fun getFile(fileId: String): BookLibFile

    suspend fun listFiles(): List<BookLibFile>

    suspend fun deleteFile(fileId: String)

}