package com.github.lucascalheiros.commom.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer

fun PdfRenderer.Page.toBitmap(): Bitmap {
    val ratio =  Resources.getSystem().displayMetrics.widthPixels.toFloat() / width
    val maxPageWidth = (width * ratio).toInt()
    val maxPageHeight = (height * ratio).toInt()
    val bitmap = Bitmap.createBitmap(maxPageWidth, maxPageHeight, Bitmap.Config.ARGB_8888)
    render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    close()
    return bitmap
}