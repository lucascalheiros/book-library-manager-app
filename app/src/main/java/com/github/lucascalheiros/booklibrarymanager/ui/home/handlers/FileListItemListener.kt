package com.github.lucascalheiros.booklibrarymanager.ui.home.handlers

import com.github.lucascalheiros.booklibrarymanager.ui.home.model.FileListItem

interface FileListItemListener {
    fun download(item: FileListItem)
    fun read(item: FileListItem)
}