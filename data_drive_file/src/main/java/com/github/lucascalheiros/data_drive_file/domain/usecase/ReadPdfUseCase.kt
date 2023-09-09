package com.github.lucascalheiros.data_drive_file.domain.usecase

import android.graphics.pdf.PdfRenderer
import com.github.lucascalheiros.common.model.interfaces.BookLibFile

interface ReadPdfUseCase {
    suspend fun pdfRendererFromFileId(fileId: String): PdfRenderer
    suspend fun registerReadProgress(fileId: String, readProgress: Int, totalPages: Int): BookLibFile
}