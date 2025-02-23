package com.github.service_synchronization.notifications

object DownloadNotificationIdGenerator {
    private const val INITIAL_ID = 0
    private const val MAX_COUNT = 100
    private var currentId = 0

    fun iterateNext(): Int {
        currentId++
        return currentId()
    }

    fun currentId(): Int {
        return INITIAL_ID + currentId % MAX_COUNT
    }
}