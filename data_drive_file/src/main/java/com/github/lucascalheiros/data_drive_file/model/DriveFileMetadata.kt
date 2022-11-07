package com.github.lucascalheiros.data_drive_file.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.common.utils.constants.AppPropertiesKeys.READ_PROGRESS
import com.github.lucascalheiros.common.utils.constants.AppPropertiesKeys.TAGS
import com.github.lucascalheiros.common.utils.constants.AppPropertiesKeys.TOTAL_PAGES
import java.time.LocalDateTime

@Entity
data class DriveFileMetadata(
    override val id: String? = null,
    override val name: String = "",
    override val createdTime: LocalDateTime? = null,
    override val modifiedTime: LocalDateTime? = null,
    val appProperties: Map<String?, String?>? = null,
    val description: String? = null,
    val mimeType: String? = null,
    val originalFilename: String? = null,
    val size: Int? = null,
    val trashed: Boolean? = null,
    val trashedTime: LocalDateTime? = null,
    @PrimaryKey val localId: Int? = null,
    override val thumbnailLink: String? = null
) : BookLibFile {
    override val readPercent: Float
        get() = readProgress.toFloat() / totalPages.toFloat()
    override val tags: List<String>
        get() = appProperties?.get(TAGS)?.split(",").orEmpty()
    override val readProgress: Int
        get() = appProperties?.get(READ_PROGRESS)?.toIntOrNull() ?: 0
    override val totalPages: Int
        get() = appProperties?.get(TOTAL_PAGES)?.toIntOrNull() ?: 1

}

