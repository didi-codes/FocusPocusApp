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

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun fetchTasks(userId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(userId)
            .collection("tasks")
            .get()
            .addOnSuccessListener { result ->
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

    fun confirmTask(userId: String, taskId: String) {
        val db = FirebaseFirestore.getInstance()
        val taskRef = db.collection("users").document(userId)
            .collection("tasks").document(taskId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(taskRef)
            val currentStreak = snapshot.getLong("streak")?.toInt() ?: 0

            transaction.update(taskRef, mapOf(
                "status" to "Confirmed",
                "streak" to currentStreak + 1
            ))
        }.addOnSuccessListener {
            Log.d("Firestore", "✅ Task confirmed with incremented streak")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "❌ Error confirming task", e)
        }
    }

    fun deleteTask(userId: String, taskId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Task deleted")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error deleting task", e)
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

