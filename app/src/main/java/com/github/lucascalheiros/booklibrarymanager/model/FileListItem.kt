package com.github.lucascalheiros.booklibrarymanager.model

import java.time.LocalDateTime

data class FileListItem(
    val name: String,

    val tags: List<String>,

    val modifiedTime: LocalDateTime?,

    val createdTime: LocalDateTime?,

    val id: String?,

    val readPercent: Float
)