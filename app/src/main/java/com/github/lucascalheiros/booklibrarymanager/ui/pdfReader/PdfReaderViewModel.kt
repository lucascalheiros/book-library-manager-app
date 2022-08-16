package com.github.lucascalheiros.booklibrarymanager.ui.pdfReader

import android.graphics.pdf.PdfRenderer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.ui.pdfReader.handlers.ReadingPageTrackerListener
import com.github.lucascalheiros.booklibrarymanager.useCase.ReadPdfUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PdfReaderViewModel(
    private val readPdfUseCase: ReadPdfUseCase
) : ViewModel() {
    val mPageTracker = MutableLiveData<ReadingPageTrackerListener>()

    private val mRenderer = MutableLiveData<PdfRenderer>()
    val renderer: LiveData<PdfRenderer> = mRenderer

    fun initializeRenderer(fileId: String) {
        viewModelScope.launch {
            mRenderer.value ?: run {
                mRenderer.value = readPdfUseCase.pdfRendererFromFileId(fileId)
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
                Log.d("testing, page tracker", page.toString())
            }
        }
    }
}