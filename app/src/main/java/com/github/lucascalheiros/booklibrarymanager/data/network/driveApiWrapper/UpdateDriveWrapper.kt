package com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper

import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile

internal interface UpdateDriveWrapper {

    suspend fun updateFileInfo(
        fileId: String,
        name: String?,
        tags: List<String>?,
        readProgress: Int?,
        totalPages: Int?
    ): BookLibFile

}