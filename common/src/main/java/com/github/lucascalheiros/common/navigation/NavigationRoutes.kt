package com.github.lucascalheiros.common.navigation

import android.net.Uri
import androidx.core.net.toUri

object NavigationRoutes {
    val homeScreen =
        "android-app://com.github.lucascalheiros.booklibrarymanager/homeFragment".toUri()
    val loginScreen =
        "android-app://com.github.lucascalheiros.booklibrarymanager/loginFragment".toUri()

    fun pdfReader(fileId: String, page: Int): Uri {
        return "android-app://com.github.lucascalheiros.booklibrarymanager/PdfReaderFragment/$fileId/$page".toUri()
    }
}