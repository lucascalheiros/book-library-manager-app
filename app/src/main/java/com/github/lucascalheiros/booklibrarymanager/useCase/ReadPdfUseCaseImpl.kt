package com.github.lucascalheiros.booklibrarymanager.useCase

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile
import com.github.lucascalheiros.booklibrarymanager.model.converter.FileListItemConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Single
class ReadPdfUseCaseImpl(
    private val fileListUseCase: FileListUseCase,
    private val fileRepository: FileRepository
) : ReadPdfUseCase {

    override suspend fun pdfRendererFromFileId(fileId: String): PdfRenderer =
        withContext(Dispatchers.IO) {
            val file = fileListUseCase.getFile(fileId)
            suspendCoroutine { continuation ->
                thread {
                    try {
                        val fileDescriptor =
                            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                        continuation.resume(PdfRenderer(fileDescriptor))
                    } catch (t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            }
        }

    override suspend fun registerReadProgress(
        fileId: String,
        readProgress: Int,
        totalPages: Int
    ): BookLibFile {
        return FileListItemConverter.from(fileRepository.updateFileInfo(fileId, readProgress = readProgress, totalPages = totalPages))
    }
}