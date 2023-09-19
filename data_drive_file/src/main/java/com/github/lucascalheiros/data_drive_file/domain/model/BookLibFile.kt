package com.github.lucascalheiros.data_drive_file.domain.model

import java.time.LocalDateTime

interface BookLibFile {
    val localId: String
    val cloudId: String?
    val name: String
    val tags: List<String>
    val modifiedTime: LocalDateTime
    val createdTime: LocalDateTime
    val readProgress: Int
    val totalPages: Int
    val readPercent: Float
        get() = readProgress.toFloat() / totalPages.toFloat()
    val thumbnailLink: String?
    val hasPendingUpdate: Boolean
    val deleted: Boolean

    override fun equals(other: Any?): Boolean
}