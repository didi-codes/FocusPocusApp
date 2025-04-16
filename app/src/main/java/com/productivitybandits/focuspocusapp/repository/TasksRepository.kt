package com.productivitybandits.focuspocusapp.repository

import com.productivitybandits.focuspocusapp.RetrofitClient
import com.productivitybandits.focuspocusapp.models.Task

class TasksRepository {

    suspend fun getTasks(): List<Task> {
        return RetrofitClient.apiService.getTasks().body() ?: emptyList()
    }

    suspend fun addTask(task: Task): Task {
        return RetrofitClient.apiService.addTask(task).body()!!
    }

    suspend fun deleteTask(id: String) {
        RetrofitClient.apiService.deleteTask(id)
    }

    suspend fun completeTask(id: String): Task {
        return RetrofitClient.apiService.completeTask(id).body()!!
    }
}