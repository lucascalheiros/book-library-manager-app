package com.github.lucascalheiros.booklibrarymanager.data.network.driveApiWrapper

internal interface CreateDriveWrapper {

    suspend fun createFile(filename: String, fileContent: java.io.File): String

}