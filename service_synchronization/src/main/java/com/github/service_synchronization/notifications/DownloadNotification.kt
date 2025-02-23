package com.github.service_synchronization.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.service_synchronization.R


fun Context.showDownloadNotification(
    notificationId: Int = DownloadNotificationIdGenerator.currentId(),
    intent: Intent? = null,
    isComplete: Boolean = false
) {
    NotificationChannels.DOWNLOAD_CHANNEL.createNotificationChannel(this)

    val pendingIntent = intent?.let {
        PendingIntent.getActivity(
            this,
            0,
            it,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val builder = NotificationCompat.Builder(this, NotificationChannels.DOWNLOAD_CHANNEL.channelId)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setAutoCancel(true)
        .let { builder ->
            if (isComplete) {
                (pendingIntent?.let { builder.setContentIntent(it) } ?: builder)
                    .setContentText(getString(R.string.download_complete))
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
            } else {
                builder.setContentTitle(getString(R.string.downloading_content))
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setProgress(0, 0, true)
            }
        }

    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }
}

fun Context.dismissCurrentDownloadNotification() {
    NotificationManagerCompat.from(this).cancel(DownloadNotificationIdGenerator.currentId())

}
