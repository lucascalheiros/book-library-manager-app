package com.github.lucascalheiros.feature_pdfreader

import com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader.PdfReaderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pdfReaderModule = module {
    viewModel { PdfReaderViewModel(get()) }
}

