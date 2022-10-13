package com.github.lucascalheiros.booklibrarymanager.data

import com.github.lucascalheiros.core.utils.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.utils.getRandomLocalDateTime
import com.github.lucascalheiros.booklibrarymanager.utils.getRandomString
import java.time.LocalDateTime

data class MockBookLibFileImpl(
    override val name: String = getRandomString(),
    override val tags: List<String> = List(3) { getRandomString(3) },
    override val modifiedTime: LocalDateTime? = getRandomLocalDateTime(),
    override val createdTime: LocalDateTime? = getRandomLocalDateTime(),
    override val id: String? = getRandomString(),
    override val readProgress: Int = 0,
    override val totalPages: Int = (1..100).random(),
    override val thumbnailLink: String? = null
) : BookLibFile
