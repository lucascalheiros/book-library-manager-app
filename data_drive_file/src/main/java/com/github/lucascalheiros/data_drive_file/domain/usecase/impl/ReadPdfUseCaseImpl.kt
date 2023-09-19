package com.github.lucascalheiros.data_drive_file.domain.usecase.impl

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.github.lucascalheiros.common.model.interfaces.BookLibFile
import com.github.lucascalheiros.data_drive_file.domain.usecase.FetchFilesUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.ReadPdfUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReadPdfUseCaseImpl(
    private val fetchFilesUseCase: FetchFilesUseCase,
    private val fileManagementUseCase: FileManagementUseCase
) : ReadPdfUseCase {

    override suspend fun pdfRendererFromFileId(fileId: String) = withContext(Dispatchers.IO) {
            val file = fetchFilesUseCase.downloadMedia(fileId)
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