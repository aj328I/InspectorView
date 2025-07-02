package com.example.inspectorviewapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo): Long

    @Update
    suspend fun updatePhoto(photo: Photo)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Query("SELECT * FROM photos WHERE projectId = :projectId ORDER BY timestamp DESC")
    fun getPhotosByProjectId(projectId: Long): Flow<List<Photo>>

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<Photo>>
} 