package com.github.lucascalheiros.data_drive_file.useCase

import android.graphics.pdf.PdfRenderer
import com.github.lucascalheiros.common.interfaces.BookLibFile

interface ReadPdfUseCase {

    suspend fun pdfRendererFromFileId(fileId: String): PdfRenderer

    suspend fun registerReadProgress(fileId: String, readProgress: Int, totalPages: Int): BookLibFile

}