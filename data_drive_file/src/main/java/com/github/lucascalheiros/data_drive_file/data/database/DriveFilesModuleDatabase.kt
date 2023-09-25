package com.github.lucascalheiros.data_drive_file.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.lucascalheiros.data_drive_file.data.converter.Converters
import com.github.lucascalheiros.data_drive_file.data.database.dao.LocalFileMetadataDao
import com.github.lucascalheiros.data_drive_file.data.model.LocalFileMetadata

@Database(entities = [LocalFileMetadata::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class DriveFilesModuleDatabase : RoomDatabase() {
    abstract fun localFileMetadataDao(): LocalFileMetadataDao
}

internal fun Context.filesMetadataDatabase(): DriveFilesModuleDatabase {
    return Room.databaseBuilder(this, DriveFilesModuleDatabase::class.java, "DriveFilesModuleDatabase")
        .fallbackToDestructiveMigration()
        .build()
}
