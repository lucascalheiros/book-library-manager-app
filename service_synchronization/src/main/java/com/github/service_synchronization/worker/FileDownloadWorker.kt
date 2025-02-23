package com.github.service_synchronization.worker

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
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
import com.github.lucascalheiros.data_drive_file.domain.usecase.FetchFilesUseCase
import com.github.lucascalheiros.data_drive_file.domain.utils.MimeTypeConstants
import com.github.service_synchronization.R
import com.github.service_synchronization.notifications.DownloadNotificationIdGenerator
import com.github.service_synchronization.notifications.NotificationChannels
import com.github.service_synchronization.notifications.dismissCurrentDownloadNotification
import com.github.service_synchronization.notifications.showDownloadNotification

class FileDownloadWorker(
    private val fileSyncUseCase: FetchFilesUseCase,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val files = fileSyncUseCase.listFiles()

            files.forEachIndexed { index, item ->
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    NotificationManagerCompat.from(applicationContext).notify(
                        NOTIFICATION_ID, createNotification(
                            applicationContext.getString(
                                R.string.notification_download_title, index + 1, files.size
                            )
                        )
                    )
                }

                val notificationId = DownloadNotificationIdGenerator.iterateNext()
                applicationContext.showDownloadNotification(notificationId, null, false)
                val intent = try {
                    val file = fileSyncUseCase.downloadMedia(item.localId)
                    val uri = FileProvider.getUriForFile(
                        applicationContext,
                        applicationContext.packageName.toString() + ".provider",
                        file
                    )
                    val mime = MimeTypeConstants.pdf

                    Intent().apply {
                        action = Intent.ACTION_VIEW
                        setDataAndType(uri, mime)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                } catch (e: Exception) {
                    applicationContext.dismissCurrentDownloadNotification()
                    return@forEachIndexed
                }

                applicationContext.showDownloadNotification(notificationId, intent, true)
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
            createNotification(applicationContext.getString(R.string.notification_download_title_not_started))
        )
    }

    private fun createNotification(title: String): Notification {
        createNotificationChannel()
        return Notification.Builder(applicationContext, NotificationChannels.DOWNLOAD_CHANNEL.channelId)
            .setSmallIcon(com.github.lucascalheiros.common.R.drawable.ic_read_book)
            .setContentTitle(title)
            .build()
    }

    private fun createNotificationChannel() {
        NotificationChannels.DOWNLOAD_CHANNEL.createNotificationChannel(applicationContext)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val WORK_NAME = "FileDownloadWorker"
        private val TAG = FileDownloadWorker::class.java.simpleName

        fun startWorker(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val worker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
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