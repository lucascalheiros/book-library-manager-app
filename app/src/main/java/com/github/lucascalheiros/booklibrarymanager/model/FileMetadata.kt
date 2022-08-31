package com.github.lucascalheiros.booklibrarymanager.model

import java.time.LocalDateTime

data class FileMetadata(
    var appProperties: Map<String?, String?>? = null,
    var createdTime: LocalDateTime? = null,
    var description: String? = null,
    var id: String? = null,
    var mimeType: String? = null,
    var modifiedTime: LocalDateTime? = null,
    var name: String = "",
    var originalFilename: String? = null,
    var size: Int? = null,
    var trashed: Boolean? = null,
    var trashedTime: LocalDateTime? = null
)