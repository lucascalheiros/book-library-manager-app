package com.github.lucascalheiros.booklibrarymanager.useCase

import android.graphics.pdf.PdfRenderer
import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile

interface ReadPdfUseCase {

    suspend fun pdfRendererFromFileId(fileId: String): PdfRenderer

    suspend fun registerReadProgress(fileId: String, readProgress: Int, totalPages: Int): BookLibFile
}