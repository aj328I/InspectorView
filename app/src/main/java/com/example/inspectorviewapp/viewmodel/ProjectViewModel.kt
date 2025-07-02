package com.example.inspectorviewapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inspectorviewapp.data.AppDatabase
import com.example.inspectorviewapp.data.Project
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val projectDao = AppDatabase.getDatabase(application).projectDao()
    val allProjects: StateFlow<List<Project>> =
        projectDao.getAllProjects().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertProject(project: Project) = viewModelScope.launch {
        projectDao.insertProject(project)
    }

    fun updateProject(project: Project) = viewModelScope.launch {
        projectDao.updateProject(project)
    }

    fun deleteProject(project: Project) = viewModelScope.launch {
        projectDao.deleteProject(project)
    }

    suspend fun getProjectById(id: Long): Project? = projectDao.getProjectById(id)
} 