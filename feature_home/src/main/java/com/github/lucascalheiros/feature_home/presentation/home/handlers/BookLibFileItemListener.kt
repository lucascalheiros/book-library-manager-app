package com.github.lucascalheiros.feature_home.presentation.home.handlers

import com.github.lucascalheiros.data_drive_file.domain.model.BookLibFile

interface BookLibFileItemListener {
    fun download(item: BookLibFile)
    fun read(item: BookLibFile)
    fun edit(item: BookLibFile)
    fun delete(item: BookLibFile)
}