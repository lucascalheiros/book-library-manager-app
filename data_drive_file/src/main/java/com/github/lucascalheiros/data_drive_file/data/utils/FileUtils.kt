package com.github.lucascalheiros.data_drive_file.data.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

internal fun loadParcelFileDescriptorFromAsset(context: Context, assetFileName: String): ParcelFileDescriptor {
    val f: File = loadFileFromAsset(context, assetFileName)
    return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY)
}

internal fun loadFileFromAsset(context: Context, assetName: String): File {
    val outFile = File(context.cacheDir, assetName)

    val buffer = context.assets.open(assetName).readBytes()
    val outStream: OutputStream = FileOutputStream(outFile)
    outStream.write(buffer)
    outStream.close()

    return outFile
}

internal fun loadFileFromInputStream(context: Context, inputStream: InputStream, fileName: String): File {
    val outFile = File(context.cacheDir, fileName)

    val buffer = inputStream.readBytes()
    val outStream: OutputStream = FileOutputStream(outFile)
    outStream.write(buffer)
    outStream.close()

    return outFile
}

internal fun getFileName(context: Context, uri: Uri): String? {
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