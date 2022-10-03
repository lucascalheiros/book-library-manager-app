package com.github.lucascalheiros.booklibrarymanager.utils

import com.github.lucascalheiros.booklibrarymanager.model.DriveFileMetadata
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.services.drive.model.File
import java.time.Instant
import java.time.ZoneId

fun File.toBookLibFile(): BookLibFile {
    return DriveFileMetadata(
        appProperties = appProperties,
        description = description,
        id = id,
        mimeType = mimeType,
        name = name,
        originalFilename = originalFilename,
        size = size,
        trashed = trashed,
        createdTime = createdTime?.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        modifiedTime = modifiedTime?.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        trashedTime = trashedTime?.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        },
        thumbnailLink = thumbnailLink
    )
}

fun GoogleSignInAccount.infoString(): String {
    return "ID: ${id}\n" +
            "Name: ${displayName}\n" +
            "ServerAuth: ${serverAuthCode}\n" +
            "ID Token: ${idToken}\n" +
            "Email: ${email}\n" +
            "PhotoUri: ${photoUrl}\n"
}