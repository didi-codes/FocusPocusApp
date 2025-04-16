package com.productivitybandits.focuspocusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productivitybandits.focuspocusapp.models.Task
import com.productivitybandits.focuspocusapp.repository.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun fetchTasks() {
        viewModelScope.launch {
            try {
                _tasks.value = repository.getTasks()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                val added = repository.addTask(task)
                _tasks.value = _tasks.value + added
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteTask(id)
                _tasks.value = _tasks.value.filterNot{ it.id == id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun completeTask(id: String) {
        viewModelScope.launch {
            try {
                val updated = repository.completeTask(id)
                _tasks.value = _tasks.value.map { if (it.id == id) updated else it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}