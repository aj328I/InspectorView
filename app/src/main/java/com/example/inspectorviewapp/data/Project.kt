package com.example.inspectorviewapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val location: String,
    val notes: String? = null,
    val pdfPath: String? = null
) 