package com.github.lucascalheiros.booklibrarymanager.model.converter

import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.model.FileDriveMetadata

object FileListItemConverter {
    private const val TAGS = "TAGS"
    private const val READ_PROGRESS = "READ_PROGRESS"
    private const val TOTAL_PAGES = "TOTAL_PAGES"

    fun from(fileDriveMetadata: FileDriveMetadata): BookLibFile {

        return BookLibFile(
            name = fileDriveMetadata.name,
            tags = fileDriveMetadata.appProperties?.get(TAGS)?.split(",").orEmpty(),
            modifiedTime = fileDriveMetadata.modifiedTime,
            createdTime = fileDriveMetadata.createdTime,
            id = fileDriveMetadata.id,
            readProgress = fileDriveMetadata.appProperties?.get(READ_PROGRESS)?.toIntOrNull() ?: 0,
            totalPages = fileDriveMetadata.appProperties?.get(TOTAL_PAGES)?.toIntOrNull() ?: 1
        )
    }

}