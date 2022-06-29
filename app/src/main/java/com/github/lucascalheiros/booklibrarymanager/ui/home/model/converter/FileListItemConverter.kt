package com.github.lucascalheiros.booklibrarymanager.ui.home.model.converter

import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItem
import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItemImpl

object FileListItemConverter {
    const val TAGS = "TAGS"
    const val READ_PERCENT = "READ_PERCENT"

    fun from(fileMetadata: FileMetadata): FileListItem {

        return FileListItemImpl(
            fileMetadata.name,
            fileMetadata.appProperties?.get(TAGS)?.split(",").orEmpty(),
            fileMetadata.modifiedTime,
            fileMetadata.createdTime,
            fileMetadata.id,
            fileMetadata.appProperties?.get(READ_PERCENT)?.toFloatOrNull() ?: 0f

            )
    }

}