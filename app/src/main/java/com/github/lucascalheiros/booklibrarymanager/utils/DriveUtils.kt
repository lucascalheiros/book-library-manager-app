package com.github.lucascalheiros.booklibrarymanager.utils

import com.github.lucascalheiros.booklibrarymanager.model.DriveFileMetadata
import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.ZoneId

fun File.toDriveFileMetadata(): DriveFileMetadata {
    return DriveFileMetadata(
        appProperties = appProperties,
        description = description,
        id = id,
        mimeType = mimeType,
        name = name,
        originalFilename = originalFilename,
        size = size,
        trashed = trashed,
        createdTime = createdTime?.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        modifiedTime = modifiedTime?.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        trashedTime = trashedTime?.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    )
}