package com.github.lucascalheiros.data_drive_file.data.database

import androidx.room.*
import com.github.lucascalheiros.data_drive_file.data.model.DriveFileMetadata

@Dao
interface DriveFileMetadataDao {

    @Query("SELECT * FROM DriveFileMetadata")
    suspend fun getAll(): List<DriveFileMetadata>

    @Query(
        "SELECT * FROM DriveFileMetadata WHERE id = :id LIMIT 1"
    )
    suspend fun findById(id: String): DriveFileMetadata?

    @Query(
        "SELECT * FROM DriveFileMetadata WHERE localId = :localId LIMIT 1"
    )
    suspend fun findByLocalId(localId: Long): DriveFileMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg files: DriveFileMetadata)

    @Delete
    suspend fun delete(file: DriveFileMetadata)

}