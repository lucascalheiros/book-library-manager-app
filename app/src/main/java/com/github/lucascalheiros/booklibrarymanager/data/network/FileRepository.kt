package com.github.lucascalheiros.booklibrarymanager.data.network


import com.google.api.client.http.FileContent
import java.io.File

interface FileRepository {

    suspend fun createFile(
        fileMetadata: com.google.api.services.drive.model.File
    ): String

    suspend fun createFile(
        fileMetadata: com.google.api.services.drive.model.File,
        mediaContent: FileContent
    ): String

    suspend fun updateFileInfo(
        fileMetadata: com.google.api.services.drive.model.File
    ): com.google.api.services.drive.model.File

    suspend fun downloadMedia(fileId: String): File

    suspend fun getFile(fileId: String): com.google.api.services.drive.model.File

    suspend fun listFiles(query: String? = null): List<com.google.api.services.drive.model.File>

    suspend fun deleteFile(fileId: String)

}