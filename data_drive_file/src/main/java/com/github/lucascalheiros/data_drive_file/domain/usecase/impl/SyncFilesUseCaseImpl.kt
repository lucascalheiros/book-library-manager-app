package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import androidx.core.net.toUri
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.data_drive_file.domain.repository.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.domain.repository.LocalFileRepository
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileSyncUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.SynchronizationProgressState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce

class SyncFilesUseCaseImpl(
    private val driveFileRepository: DriveFileRepository,
    private val localFileRepository: LocalFileRepository
) : FileSyncUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun CoroutineScope.syncFiles() = produce {
        val listFiles = localFileRepository.listFiles(onlyValid = false).filter { it.hasPendingUpdate }
        logDebug(TAG, "::syncFiles\n" +listFiles.joinToString("\n"))
        val total = listFiles.count()
        listFiles.forEachIndexed { index, bookLibFile ->
            send(SynchronizationProgressState(index + 1, total))
            if (deleteIfNecessary(bookLibFile)) {
                return@forEachIndexed
            }
            val cloudId = uploadFileIfNecessary(bookLibFile)
            updateDriveFileInfo(cloudId)
        }
        close()
    }

    private suspend fun deleteIfNecessary(bookLibFile: BookLibFile): Boolean {
        logDebug(TAG, "::deleteIfNecessary $bookLibFile" )
        if (bookLibFile.deleted) {
            bookLibFile.cloudId?.let { driveFileRepository.deleteFile(it) }
            localFileRepository.hardDelete(bookLibFile.localId)
            return true
        }
        return false
    }

    private suspend fun uploadFileIfNecessary(bookLibFile: BookLibFile): String {
        logDebug(TAG, "::uploadFileIfNecessary $bookLibFile" )
        val cloudId = bookLibFile.cloudId
        if (cloudId != null) {
            return cloudId
        }
        logDebug(TAG, "::uploadFileIfNecessary uploading" )
        val localFile = localFileRepository.downloadMedia(bookLibFile.localId)!!
        return driveFileRepository.uploadFile(localFile.toUri()).also { idFromCloud ->
            localFileRepository.updateCloudId(bookLibFile.localId, idFromCloud)
        }
    }

    private suspend fun updateDriveFileInfo(cloudId: String) {
        logDebug(TAG, "::updateDriveFileInfo cloudId: $cloudId" )
        val bookLibFile = localFileRepository.getByCloudId(cloudId)!!
        logDebug(TAG, "::updateDriveFileInfo bookLibFile: $bookLibFile" )
        driveFileRepository.syncFileInfo(bookLibFile)
        localFileRepository.updatePendingUpdate(bookLibFile.localId, false)
    }

    companion object {
        private val TAG = SyncFilesUseCaseImpl::class.java.simpleName
    }
}