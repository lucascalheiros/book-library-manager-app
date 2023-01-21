package com.github.lucascalheiros.booklibrarymanager.data.network

import com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper.CreateDriveWrapper
import com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper.CreateDriveWrapperImpl
import com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper.UpdateDriveWrapper
import com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper.UpdateDriveWrapperImpl
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.utils.constants.MimeTypeConstants
import com.github.lucascalheiros.booklibrarymanager.utils.driveUtils.DriveQueryBuilder
import com.github.lucascalheiros.booklibrarymanager.utils.driveUtils.toBookLibFile


class BookLibFileRepositoryImpl private constructor(
    private val driveFileRepository: DriveFileRepository,
    private val createDriveWrapper: CreateDriveWrapper,
    private val updateDriveWrapper: UpdateDriveWrapper
) :
    UpdateDriveWrapper by updateDriveWrapper,
    CreateDriveWrapper by createDriveWrapper,
    BookLibFileRepository {

    constructor(
        driveFileRepository: DriveFileRepository
    ) : this(
        driveFileRepository,
        CreateDriveWrapperImpl(driveFileRepository),
        UpdateDriveWrapperImpl(driveFileRepository)
    )

    override suspend fun downloadMedia(fileId: String): java.io.File {
        return driveFileRepository.downloadMedia(fileId)
    }

    override suspend fun getFile(fileId: String): BookLibFile {
        return driveFileRepository.getFile(fileId).toBookLibFile()
    }

    override suspend fun listFiles(): List<BookLibFile> {
        return driveFileRepository.listFiles(
            DriveQueryBuilder().mimeTypeEquals(MimeTypeConstants.pdf).build()
        ).map { it.toBookLibFile() }
    }

    override suspend fun deleteFile(fileId: String) {
        driveFileRepository.deleteFile(fileId)
    }

}