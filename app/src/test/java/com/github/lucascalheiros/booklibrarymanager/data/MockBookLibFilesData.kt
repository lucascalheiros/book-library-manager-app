package com.github.lucascalheiros.booklibrarymanager.data

object MockBookLibFilesData {
    val file1 = MockBookLibFileImpl()
    val file2 = MockBookLibFileImpl()
    val file3 = MockBookLibFileImpl()
    val items1And2 = listOf(
        file1,
        file2
    )

    val items2And3 = listOf(
        file2,
        file3
    )

}