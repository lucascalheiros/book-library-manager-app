package com.github.lucascalheiros.booklibrarymanager.ui.home.handlers

import com.github.lucascalheiros.booklibrarymanager.model.BookLibFile

interface FileListItemListener {
    fun download(item: BookLibFile)
    fun read(item: BookLibFile)
}