package com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader.handlers

interface ReadingPageTrackerListener {
    fun onPageReadChange(page: Int)
}