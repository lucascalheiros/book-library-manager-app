package com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader

import android.graphics.pdf.PdfRenderer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.data_drive_file.domain.usecase.ReadPdfUseCase
import com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader.handlers.ReadingPageTrackerListener
import kotlinx.coroutines.launch

class PdfReaderViewModel(
    private val readPdfUseCase: ReadPdfUseCase
) : ViewModel() {
    val mPageTracker = MutableLiveData<ReadingPageTrackerListener>()

    private val mRenderer = MutableLiveData<PdfRenderer?>()
    val renderer: LiveData<PdfRenderer?> = mRenderer

    val initialPdfPagePosition = MutableLiveData<Int>()

    lateinit var fileId: String

    fun initializeRenderer(fileId: String, initialPage: Int) {
        this.fileId = fileId
        viewModelScope.launch {
            if (mRenderer.value == null) {
                mRenderer.value = readPdfUseCase.pdfRendererFromFileId(fileId)
            }
            if (initialPdfPagePosition.value != -1) {
                initialPdfPagePosition.value = initialPage - 1
            }
        }
    }

    fun closeRenderer() {
        mRenderer.value?.close()
        mRenderer.value = null
    }

    init {
        mPageTracker.value = object : ReadingPageTrackerListener {
            override fun onPageReadChange(page: Int) {
                initialPdfPagePosition.value = -1
                viewModelScope.launch {
                    readPdfUseCase.registerReadProgress(fileId, page, mRenderer.value?.pageCount ?: 1)
                }
            }
        }
    }
}