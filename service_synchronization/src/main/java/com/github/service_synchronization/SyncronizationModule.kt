package com.github.service_synchronization

import com.github.service_synchronization.worker.FileSynchronizationWorker
import com.github.service_synchronization.worker.FileDownloadWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module


val synchronizationModule = module {
    workerOf(::FileSynchronizationWorker)
    workerOf(::FileDownloadWorker)
}
