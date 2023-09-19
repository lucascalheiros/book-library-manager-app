package com.github.lucascalheiros.data_drive_file.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface FileSyncUseCase {
     fun CoroutineScope.syncFiles(): ReceiveChannel<SynchronizationProgressState>
}

data class SynchronizationProgressState(val current: Int, val total: Int)
