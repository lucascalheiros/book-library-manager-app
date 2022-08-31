package com.github.lucascalheiros.booklibrarymanager.model.converter

import com.github.lucascalheiros.booklibrarymanager.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata

object FileListItemConverter {
    private const val TAGS = "TAGS"
    private const val READ_PROGRESS = "READ_PROGRESS"
    private const val TOTAL_PAGES = "TOTAL_PAGES"

    fun from(fileMetadata: FileMetadata): FileListItem {

        return FileListItem(
            name = fileMetadata.name,
            tags = fileMetadata.appProperties?.get(TAGS)?.split(",").orEmpty(),
            modifiedTime = fileMetadata.modifiedTime,
            createdTime = fileMetadata.createdTime,
            id = fileMetadata.id,
            readProgress = fileMetadata.appProperties?.get(READ_PROGRESS)?.toIntOrNull() ?: 0,
            totalPages = fileMetadata.appProperties?.get(TOTAL_PAGES)?.toIntOrNull() ?: 1
        )
    }

}