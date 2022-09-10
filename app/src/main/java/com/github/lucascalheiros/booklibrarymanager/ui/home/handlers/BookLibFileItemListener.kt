package com.github.lucascalheiros.booklibrarymanager.ui.home.handlers

import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile

interface BookLibFileItemListener {
    fun download(item: BookLibFile)
    fun read(item: BookLibFile)
    fun edit(item: BookLibFile)
    fun delete(item: BookLibFile)
}