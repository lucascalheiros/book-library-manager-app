package com.github.lucascalheiros.booklibrarymanager.model.converter

import com.github.lucascalheiros.booklibrarymanager.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata

object FileListItemConverter {
    private const val TAGS = "TAGS"
    private const val READ_PERCENT = "READ_PERCENT"

    fun from(fileMetadata: FileMetadata): FileListItem {

        return FileListItem(
            name = fileMetadata.name,
            tags = fileMetadata.appProperties?.get(TAGS)?.split(",").orEmpty(),
            modifiedTime = fileMetadata.modifiedTime,
            createdTime = fileMetadata.createdTime,
            id = fileMetadata.id,
            readPercent = fileMetadata.appProperties?.get(READ_PERCENT)?.toFloatOrNull() ?: 0f
        )
    }

}