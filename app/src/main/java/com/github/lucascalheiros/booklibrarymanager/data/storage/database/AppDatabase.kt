package com.github.lucascalheiros.booklibrarymanager.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.lucascalheiros.booklibrarymanager.model.DriveFileMetadata
import com.github.lucascalheiros.booklibrarymanager.model.converter.Converters


@Database(entities = [DriveFileMetadata::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driveFileMetadataDao(): DriveFileMetadataDao
}
