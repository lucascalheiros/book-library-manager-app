package com.github.lucascalheiros.booklibrarymanager.data

import com.github.lucascalheiros.common_test.utils.getRandomLocalDateTime
import com.github.lucascalheiros.common_test.utils.getRandomString
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import java.time.LocalDateTime

data class MockBookLibFileImpl(
    override val name: String = getRandomString(),
    override val tags: List<String> = List(3) { getRandomString(3) },
    override val modifiedTime: LocalDateTime = getRandomLocalDateTime(),
    override val createdTime: LocalDateTime = getRandomLocalDateTime(),
    override val localId: String = getRandomString(),
    override val readProgress: Int = 0,
    override val totalPages: Int = (1..100).random(),
    override val thumbnailLink: String? = null,
    override val hasPendingUpdate: Boolean = false,
    override val cloudId: String = getRandomString(),
    override val deleted: Boolean = false
) : BookLibFile
