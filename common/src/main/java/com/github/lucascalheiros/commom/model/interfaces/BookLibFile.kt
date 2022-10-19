package com.github.lucascalheiros.common.interfaces

import java.time.LocalDateTime

interface BookLibFile {
    val name: String
    val tags: List<String>
    val modifiedTime: LocalDateTime?
    val createdTime: LocalDateTime?
    val id: String?
    val readProgress: Int
    val totalPages: Int
    val readPercent: Float
        get() = readProgress.toFloat() / totalPages.toFloat()
    val thumbnailLink: String?

    override fun equals(other: Any?): Boolean
}