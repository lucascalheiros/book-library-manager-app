package com.github.lucascalheiros.booklibrarymanager.useCase

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class ReadPdfUseCaseImpl(
    private val fileListUseCase: FileListUseCase,
    private val fileManagementUseCase: FileManagementUseCase
) : ReadPdfUseCase {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun pdfRendererFromFileId(fileId: String) = withContext(Dispatchers.IO) {
            val file = fileListUseCase.downloadMedia(fileId)
            val fileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            PdfRenderer(fileDescriptor)
        }

    override suspend fun registerReadProgress(
        fileId: String,
        readProgress: Int,
        totalPages: Int
    ): BookLibFile {
        return fileManagementUseCase.updateFileInfo(
            fileId,
            readProgress = readProgress,
            totalPages = totalPages
        )
    }

}