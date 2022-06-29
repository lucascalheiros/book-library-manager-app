package com.github.lucascalheiros.booklibrarymanager.ui.home.model

import java.time.LocalDateTime

data class FileListItemImpl(
    override val name: String,
    override val tags: List<String>,
    override val modifiedTime: LocalDateTime?,
    override val createdTime: LocalDateTime?,
    override val id: String?,
    override val readPercent: Float
) : FileListItem
