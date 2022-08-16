package com.github.lucascalheiros.booklibrarymanager.useCase

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Single
class ReadPdfUseCaseImpl(
    private val fileListUseCase: FileListUseCase
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
}