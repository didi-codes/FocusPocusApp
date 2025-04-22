package com.productivitybandits.taskmanager

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getTasks() {
        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Firestore", "Task: ${document.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching tasks", e)
            }
    }
}
