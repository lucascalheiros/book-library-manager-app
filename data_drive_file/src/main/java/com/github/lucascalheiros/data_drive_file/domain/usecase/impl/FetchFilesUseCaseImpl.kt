package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_drive_file.domain.repository.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.domain.repository.FileCacheRepository
import com.github.lucascalheiros.data_drive_file.domain.repository.LocalFileRepository
import com.github.lucascalheiros.data_drive_file.domain.usecase.FetchFilesUseCase
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.lang.Exception

class FetchFilesUseCaseImpl(
    private val fileCacheRepository: FileCacheRepository,
    private val localFileRepository: LocalFileRepository,
    private val driveFileRepository: DriveFileRepository
) : FetchFilesUseCase {

    override fun listFilesFlow(): Flow<List<BookLibFile>> {
        return localFileRepository.listFilesFlow()
    }

    override suspend fun listFiles(): List<BookLibFile> {
        try {
            fetchFiles()
        } catch (e: Exception) {
            logError(TAG, "::listFiles", e)
        }
        return localFileRepository.listFiles()
    }

    override suspend fun fetchFiles() {
        val driveFilesMap = driveFileRepository.listFiles().groupBy { it.cloudId }
        val localFilesMap = localFileRepository.listFiles(onlyValid = false).groupBy { it.cloudId }
        val keysSet = (driveFilesMap.keys + localFilesMap.keys).toSet()
        keysSet.forEach { cloudId ->
            if (cloudId != null) {
                val driveFile = driveFilesMap[cloudId]?.firstOrNull()
                val localFile = localFilesMap[cloudId]?.firstOrNull()
                if (localFile?.deleted == true) {
                    localFile.cloudId?.let { driveFileRepository.deleteFile(it) }
                } else if (driveFile == null && localFile != null) {
                    localFileRepository.softDelete(localFile.localId)
                } else if (driveFile != null && localFile != null) {
                    if (driveFile.modifiedTime > localFile.modifiedTime) {
                        localFileRepository.insert(driveFile)
                        localFileRepository.softDelete(localFile.localId)
                    }
                } else if (driveFile != null) {
                    localFileRepository.insert(driveFile)
                }
            }
        }
    }

    override suspend fun downloadMedia(fileId: String): File {
        return localFileRepository.downloadMedia(fileId)
            ?: localFileRepository.getByLocalId(fileId)?.cloudId!!.let { cloudId ->
                fileCacheRepository.getFile(cloudId) ?: downloadFromDriveAndCache(cloudId)
            }
    }

    private suspend fun downloadFromDriveAndCache(cloudId: String): File {
        return driveFileRepository.downloadMedia(cloudId).also {
            fileCacheRepository.writeFile(cloudId, it)
        }
    }

    companion object {
        private val TAG: String = FetchFilesUseCaseImpl::class.java.simpleName
    }
}