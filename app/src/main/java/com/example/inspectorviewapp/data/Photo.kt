package com.example.inspectorviewapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val filePath: String,
    val note: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
) 