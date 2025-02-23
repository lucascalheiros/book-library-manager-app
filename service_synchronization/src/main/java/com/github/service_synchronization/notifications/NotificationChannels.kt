package com.github.service_synchronization.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.github.service_synchronization.R

enum class NotificationChannels(
    val channelId: String,
    val nameRes: Int,
    val descriptionRes: Int,
    val importance: Int
) {
    DOWNLOAD_CHANNEL("DOWNLOAD_CHANNEL", R.string.download_notifications, R.string.notifications_for_download_progress, NotificationManager.IMPORTANCE_LOW),
    SYNC_CHANNEL("SYNC_CHANNEL", R.string.notification_sync_channel_title, R.string.notification_sync_channel_descriptions, NotificationManager.IMPORTANCE_LOW);

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(channelId, context.getString(nameRes), importance).apply {
            this.description = context.getString(descriptionRes)
        }
        context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

}

