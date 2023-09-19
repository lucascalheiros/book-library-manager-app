package com.github.lucascalheiros.data_drive_file.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import java.time.LocalDateTime
import java.util.UUID

@Entity
internal data class LocalFileMetadata(
    @PrimaryKey
    override val localId: String = UUID.randomUUID().toString(),
    override val cloudId: String? = null,
    override val name: String = "",
    override val createdTime: LocalDateTime = LocalDateTime.now(),
    override val modifiedTime: LocalDateTime = LocalDateTime.now(),
    override val readProgress: Int = 0,
    override val totalPages: Int = 1,
    override val thumbnailLink: String? = null,
    override val hasPendingUpdate: Boolean = true,
    override val deleted: Boolean = false,
    val tagsString: String = "",
) : BookLibFile {

    override val readPercent: Float
        get() = readProgress.toFloat() / totalPages.toFloat()

    override val tags: List<String>
        get() = tagsString.tagsFromString()

    companion object {
        fun String.tagsFromString(): List<String> {
            return this.split(",").filter { it.isNotBlank() }
        }

        fun List<String>.tagsToString(): String {
            return this.joinToString(",")
        }
    }
}
