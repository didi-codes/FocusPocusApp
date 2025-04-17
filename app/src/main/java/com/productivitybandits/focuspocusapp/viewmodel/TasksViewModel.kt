package com.productivitybandits.focuspocusapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.productivitybandits.focuspocusapp.models.Task
import com.productivitybandits.focuspocusapp.repository.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Possible duplicates
class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun fetchTasks(userId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the user's 'tasks' subcollection
        db.collection("users")
            .document(userId) // The specific user document
            .collection("tasks") // The tasks subcollection
            .get()
            .addOnSuccessListener { result ->
                // Loop through each task document in the tasks subcollection
                for (document in result) {
                    val task = document.toObject(com.productivitybandits.taskmanager.Task::class.java)
                    Log.d("Firestore", "${task.title} - ${task.progress}")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting tasks", e)
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