package com.github.service_synchronization.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
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
                        with(NotificationManagerCompat.from(applicationContext)) {
                            notify(NOTIFICATION_ID, createNotification(applicationContext.getString(
                                R.string.notification_sync_title, it.current, it.total)))
                        }
                    }
                }
            }
            logDebug("FileSynchronizationWorker", "workexecuted")
            Result.success()
        } catch (error: Throwable) {
            logError("FileSynchronizationWorker", "failed", error)
            Result.failure()
        } finally {
            NotificationManagerCompat.from(applicationContext).cancel(NOTIFICATION_ID)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(applicationContext.getString(R.string.notification_sync_title_not_started))
        )
    }

    private fun createNotification(title: String) : Notification {
        createNotificationChannel()
        return Notification.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(com.github.lucascalheiros.common.R.drawable.ic_read_book)
            .setContentTitle(title)
            .build()
    }

    private fun createNotificationChannel() {
        val name = applicationContext.getString(R.string.notification_sync_channel_title)
        val descriptionText = applicationContext.getString(R.string.notification_sync_channel_descriptions)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance)
        mChannel.description = descriptionText
        val notificationManager = getSystemService(applicationContext, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(mChannel)
    }

    companion object {
        private const val NOTIFICATION_ID = 1000
        private const val NOTIFICATION_CHANNEL = 1000.toString()

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
                    "FileSynchronizationWorker",
                    ExistingWorkPolicy.REPLACE,
                    worker
                )
        }
    }
}