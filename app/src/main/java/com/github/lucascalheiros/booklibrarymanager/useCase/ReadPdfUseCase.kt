package com.github.lucascalheiros.booklibrarymanager.useCase

import android.graphics.pdf.PdfRenderer

interface ReadPdfUseCase {

    suspend fun pdfRendererFromFileId(fileId: String): PdfRenderer
}