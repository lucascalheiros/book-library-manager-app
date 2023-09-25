package com.github.lucascalheiros.data_drive_file.data.database.dao

import androidx.room.*
import com.github.lucascalheiros.data_drive_file.data.model.LocalFileMetadata
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LocalFileMetadataDao {

    @Query("SELECT * FROM LocalFileMetadata WHERE deleted = 0 OR deleted != :onlyValid")
    fun getAllFlow(onlyValid: Boolean): Flow<List<LocalFileMetadata>>

    @Query("SELECT * FROM LocalFileMetadata WHERE deleted = 0 OR deleted != :onlyValid")
    suspend fun getAll(onlyValid: Boolean): List<LocalFileMetadata>

    @Query("SELECT * FROM LocalFileMetadata WHERE localId = :localId LIMIT 1")
    suspend fun findByLocalId(localId: String): LocalFileMetadata?

    @Query("SELECT * FROM LocalFileMetadata WHERE cloudId = :cloudId LIMIT 1")
    suspend fun findByCloudId(cloudId: String): LocalFileMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg files: LocalFileMetadata)

    @Query("SELECT * FROM LocalFileMetadata WHERE hasPendingUpdate = 1")
    fun filesPendingSync(): Flow<List<LocalFileMetadata>>

    @Query("UPDATE LocalFileMetadata SET cloudId = :cloudId WHERE localId = :localId")
    suspend fun updateCloudId(localId: String, cloudId: String)

    @Query("UPDATE LocalFileMetadata SET hasPendingUpdate = :state WHERE localId = :localId")
    suspend fun updatePendingUpdate(localId: String, state: Boolean)

    @Query("DELETE FROM LocalFileMetadata WHERE localId = :localId")
    suspend fun hardDelete(localId: String)

    @Query("UPDATE LocalFileMetadata SET deleted = 1 , hasPendingUpdate = 1 WHERE localId = :localId")
    suspend fun softDelete(localId: String)

}