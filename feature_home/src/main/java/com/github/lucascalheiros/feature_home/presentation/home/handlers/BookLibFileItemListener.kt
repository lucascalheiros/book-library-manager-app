package com.github.lucascalheiros.feature_home.presentation.home.handlers

import com.github.lucascalheiros.common.model.interfaces.BookLibFile

interface BookLibFileItemListener {
    fun download(item: BookLibFile)
    fun read(item: BookLibFile)
    fun edit(item: BookLibFile)
    fun delete(item: BookLibFile)
}