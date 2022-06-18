package com.github.lucascalheiros.booklibrarymanager.utils

import android.content.Context
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream
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
