package com.github.lucascalheiros.data_drive_file.data.model.adapter

import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.data_drive_file.data.constants.AppPropertiesKeys
import com.github.lucascalheiros.data_drive_file.data.model.LocalFileMetadata
import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.ZoneId

fun File.toBookLibFile(): BookLibFile {
    return LocalFileMetadata(
        cloudId = id,
        name = name,
        createdTime = (createdTime?.value ?: System.currentTimeMillis()).let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        modifiedTime = (appProperties?.get(AppPropertiesKeys.UPDATE_TIME)?.toLongOrNull() ?: System.currentTimeMillis()).let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        thumbnailLink = thumbnailLink,
        readProgress = appProperties?.get(AppPropertiesKeys.READ_PROGRESS)?.toIntOrNull() ?: 0,
        totalPages = appProperties?.get(AppPropertiesKeys.TOTAL_PAGES)?.toIntOrNull() ?: 1,
        hasPendingUpdate = false,
        deleted = false
    )
}
