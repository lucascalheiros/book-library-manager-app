package com.github.lucascalheiros.common.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun loadParcelFileDescriptorFromAsset(context: Context, assetFileName: String): ParcelFileDescriptor {
    val f: File = loadFileFromAsset(context, assetFileName)
    return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY)
}

fun loadFileFromAsset(context: Context, assetName: String): File {
    val outFile = File(context.cacheDir, assetName)

    val buffer = context.assets.open(assetName).readBytes()
    val outStream: OutputStream = FileOutputStream(outFile)
    outStream.write(buffer)
    outStream.close()

    return outFile
}

fun loadFileFromInputStream(context: Context, inputStream: InputStream, fileName: String): File {
    val outFile = File(context.cacheDir, fileName)

    val buffer = inputStream.readBytes()
    val outStream: OutputStream = FileOutputStream(outFile)
    outStream.write(buffer)
    outStream.close()

    return outFile
}

fun getFileName(context: Context, uri: Uri): String? {
    val contentResolver = context.contentResolver

    val cursor: Cursor? = contentResolver.query(
        uri, null, null, null, null, null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            return it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    return null
}