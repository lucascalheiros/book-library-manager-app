package com.github.lucascalheiros.booklibrarymanager.model

import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class FileMetadata(
    var appProperties: Map<String?, String?>? = null,

    var createdTime: LocalDateTime? = null,

    var description: String? = null,

    var id: String? = null,

    var mimeType: String? = null,

    var modifiedTime: LocalDateTime? = null,

    var name: String? = null,

    var originalFilename: String? = null,

    var size: Int? = null,

    var trashed: Boolean? = null,

    var trashedTime: LocalDateTime? = null
) {
    companion object {
        fun fromGoogleDrive(file: File): FileMetadata {

            return FileMetadata().apply {
                appProperties = file.appProperties
                description = file.description
                id = file.id
                mimeType = file.mimeType
                name = file.name
                originalFilename = file.originalFilename
                size = file.size
                trashed = file.trashed
                createdTime =
                    Instant.ofEpochMilli(file.createdTime.value).atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                modifiedTime =
                    Instant.ofEpochMilli(file.modifiedTime.value).atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                trashedTime =
                    Instant.ofEpochMilli(file.trashedTime.value).atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
            }
        }
    }
}

