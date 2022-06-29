package com.github.lucascalheiros.booklibrarymanager.ui.home.model

import java.time.LocalDateTime

interface FileListItem {
    val name: String
    val tags: List<String>
    val modifiedTime: LocalDateTime?
    val createdTime: LocalDateTime?
    val id: String?
    val readPercent: Float

    override fun equals(other: Any?): Boolean
}
