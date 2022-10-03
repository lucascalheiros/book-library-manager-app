package com.github.lucascalheiros.booklibrarymanager.utils

import android.util.Log

fun logDebug(tag: String, message: String, error: Throwable? = null) {
    logDebug(listOf(tag), message, error)
}

fun logDebug(tags: List<String?>, message: String, error: Throwable? = null) {
    val tag = tags.filterNotNull().joinToString(separator = ",", prefix = "[", postfix = "]")
    if (error != null) {
        Log.d(tag, message, error)
    } else {
        Log.d(tag, message)
    }
}

fun logError(tag: String, message: String, error: Throwable? = null) {
    logError(listOf(tag), message, error)
}

fun logError(tags: List<String?>, message: String, error: Throwable? = null) {
    val tag = tags.filterNotNull().joinToString(separator = ",", prefix = "[", postfix = "]")
    if (error != null) {
        Log.e(tag, message, error)
    } else {
        Log.e(tag, message)
    }
}