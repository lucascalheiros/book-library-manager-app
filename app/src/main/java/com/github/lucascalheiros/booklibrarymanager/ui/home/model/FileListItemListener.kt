package com.github.lucascalheiros.booklibrarymanager.ui.home.model

interface FileListItemListener {
    fun download(item: FileListItem)
    fun read(item: FileListItem)

}