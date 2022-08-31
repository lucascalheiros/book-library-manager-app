package com.github.lucascalheiros.booklibrarymanager.model.converter

import com.github.lucascalheiros.booklibrarymanager.model.FileDriveMetadata
import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.ZoneId

object FileMetadataConverter {
    fun from(file: File): FileDriveMetadata {

        return FileDriveMetadata(
            appProperties = file.appProperties,
            description = file.description,
            id = file.id,
            mimeType = file.mimeType,
            name = file.name,
            originalFilename = file.originalFilename,
            size = file.size,
            trashed = file.trashed,
            createdTime = file.createdTime?.value?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            },
            modifiedTime = file.modifiedTime?.value?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            },
            trashedTime = file.trashedTime?.value?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
        )
    }

}