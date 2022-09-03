package com.github.lucascalheiros.booklibrarymanager.model.handlers

import com.github.lucascalheiros.booklibrarymanager.model.interfaces.BookLibFile

interface BookLibFileItemListener {
    fun download(item: BookLibFile)
    fun read(item: BookLibFile)
}