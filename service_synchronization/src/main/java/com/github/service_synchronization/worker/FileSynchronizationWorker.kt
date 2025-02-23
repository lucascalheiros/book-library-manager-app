package com.github.service_synchronization.worker

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileSyncUseCase
import com.github.service_synchronization.R
import com.github.service_synchronization.notifications.NotificationChannels
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope

class FileSynchronizationWorker(
    private val fileSyncUseCase: FileSyncUseCase,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            with(fileSyncUseCase) {
                coroutineScope {
                    val fileSyncChannel = syncFiles()
                    fileSyncChannel.consumeEach {
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            NotificationManagerCompat.from(applicationContext).notify(
                                NOTIFICATION_ID, createNotification(
                                    applicationContext.getString(
                                        R.string.notification_sync_title, it.current, it.total
                                    )
                                )
                            )
                        }
                    }
                }
            }
            logDebug(TAG, "::doWork")
            Result.success()
        } catch (error: Throwable) {
            logError(TAG, "::doWork failed", error)
            Result.failure()
        } finally {
            NotificationManagerCompat.from(applicationContext).cancel(NOTIFICATION_ID)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID,
            createNotification(applicationContext.getString(R.string.notification_sync_title_not_started))
        )
    }

    private fun createNotification(title: String): Notification {
        createNotificationChannel()
        return Notification.Builder(applicationContext, NotificationChannels.SYNC_CHANNEL.channelId)
            .setSmallIcon(com.github.lucascalheiros.common.R.drawable.ic_read_book)
            .setContentTitle(title)
            .build()
    }

    private fun createNotificationChannel() {
        NotificationChannels.SYNC_CHANNEL.createNotificationChannel(applicationContext)
    }
    companion object {
        private const val NOTIFICATION_ID = 1000
        private const val WORK_NAME = "FileSynchronizationWorker"
        private val TAG = FileSynchronizationWorker::class.java.simpleName

        fun startWorker(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val worker = OneTimeWorkRequestBuilder<FileSynchronizationWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    worker
                )
        }
    }
}