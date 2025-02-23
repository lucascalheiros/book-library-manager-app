package com.github.lucascalheiros.feature_home.presentation.home.model

data class BookLibUiModel(
    val localId: String,
    val cloudId: String?,
    val thumbnailLink: String?,
    val name: String,
    val tags: List<String>,
    val readPercent: Float,
    val onDownload: () -> Unit,
    val onRead: () -> Unit,
    val onEdit: () -> Unit,
    val onDelete: () -> Unit
)
