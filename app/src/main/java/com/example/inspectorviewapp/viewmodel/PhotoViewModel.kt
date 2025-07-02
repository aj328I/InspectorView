package com.example.inspectorviewapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inspectorviewapp.data.AppDatabase
import com.example.inspectorviewapp.data.Photo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val photoDao = AppDatabase.getDatabase(application).photoDao()
    val allPhotos: StateFlow<List<Photo>> =
        photoDao.getAllPhotos().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertPhoto(photo: Photo) = viewModelScope.launch {
        photoDao.insertPhoto(photo)
    }

    fun updatePhoto(photo: Photo) = viewModelScope.launch {
        photoDao.updatePhoto(photo)
    }

    fun deletePhoto(photo: Photo) = viewModelScope.launch {
        photoDao.deletePhoto(photo)
    }

    fun getPhotosByProjectId(projectId: Long): StateFlow<List<Photo>> =
        photoDao.getPhotosByProjectId(projectId).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
} 