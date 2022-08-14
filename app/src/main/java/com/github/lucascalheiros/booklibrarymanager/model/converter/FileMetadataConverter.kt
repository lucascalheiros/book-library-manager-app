package com.github.lucascalheiros.booklibrarymanager.model.converter

import com.github.lucascalheiros.booklibrarymanager.model.FileMetadata
import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.ZoneId

object FileMetadataConverter {
    fun from(file: File): FileMetadata {

        return FileMetadata(
            appProperties = file.appProperties,
            description = file.description,
            id = file.id,
            mimeType = file.mimeType,
            name = file.name,
            originalFilename = file.originalFilename,
            size = file.size,
            trashed = file.trashed,
            createdTime = Instant.ofEpochMilli(file.createdTime.value)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
            modifiedTime = Instant.ofEpochMilli(file.modifiedTime.value)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
            trashedTime = Instant.ofEpochMilli(file.trashedTime.value)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }

}