package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile
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
        if (!driveFileRepository.isDriveAvailable()) {
            close()
            return@produce
        }
        val listFiles = localFileRepository.listFiles(onlyValid = false).filter { it.hasPendingUpdate }
        logDebug(TAG, "::syncFiles\n" + listFiles.joinToString("\n"))
        val total = listFiles.count()
        listFiles.forEachIndexed { index, bookLibFile ->
            logDebug(TAG, "::syncFiles index: $index, bookLibFile $bookLibFile")
            send(SynchronizationProgressState(index + 1, total))
            if (deleteIfNecessary(bookLibFile)) {
                return@forEachIndexed
            }
            uploadFileIfNecessary(bookLibFile)
            updateDriveFileInfoIfNecessary(bookLibFile)
        }
        logDebug(TAG, "::syncFiles before close")
        close()
    }

    private suspend fun deleteIfNecessary(bookLibFile: BookLibFile): Boolean {
        val updatedBookLibFile = localFileRepository.getByLocalId(bookLibFile.localId) ?: return true
        logDebug(TAG, "::deleteIfNecessary updatedBookLibFile $updatedBookLibFile")
        if (bookLibFile.deleted) {
            bookLibFile.cloudId?.let { driveFileRepository.deleteFile(it) }
            localFileRepository.hardDelete(bookLibFile.localId)
            return true
        }
        return false
    }

    private suspend fun uploadFileIfNecessary(bookLibFile: BookLibFile) {
        val updatedBookLibFile = localFileRepository.getByLocalId(bookLibFile.localId) ?: return
        logDebug(TAG, "::uploadFileIfNecessary updatedBookLibFile $updatedBookLibFile")
        val cloudId = updatedBookLibFile.cloudId
        if (cloudId != null) {
            return
        }
        logDebug(TAG, "::uploadFileIfNecessary uploading")
        val localFile = localFileRepository.downloadMedia(bookLibFile.localId)!!
        driveFileRepository.uploadFile(localFile, bookLibFile).also { idFromCloud ->
            localFileRepository.updateCloudId(bookLibFile.localId, idFromCloud)
            localFileRepository.updatePendingUpdate(bookLibFile.localId, false)
        }
    }

    private suspend fun updateDriveFileInfoIfNecessary(bookLibFile: BookLibFile) {
        val updatedBookLibFile = localFileRepository.getByLocalId(bookLibFile.localId) ?: return
        logDebug(TAG, "::updateDriveFileInfo updatedBookLibFile: $updatedBookLibFile")
        if (!updatedBookLibFile.hasPendingUpdate) {
            return
        }
        driveFileRepository.syncFileInfo(updatedBookLibFile)
        localFileRepository.updatePendingUpdate(updatedBookLibFile.localId, false)
    }

    companion object {
        private val TAG = SyncFilesUseCaseImpl::class.java.simpleName
    }
}