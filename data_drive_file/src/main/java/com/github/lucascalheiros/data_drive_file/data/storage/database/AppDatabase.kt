package com.github.lucascalheiros.data_drive_file.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.lucascalheiros.data_drive_file.data.model.DriveFileMetadata
import com.github.lucascalheiros.data_drive_file.data.storage.converter.Converters


@Database(entities = [DriveFileMetadata::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driveFileMetadataDao(): DriveFileMetadataDao
}
