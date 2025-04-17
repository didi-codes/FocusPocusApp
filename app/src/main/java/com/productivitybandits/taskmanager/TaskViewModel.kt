package com.productivitybandits.taskmanager

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

//Called instance of db in each function instead of a static/global variable due to memory leaks.
// =============================
// Fetch All Tasks
// =============================
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
                val task = document.toObject(Task::class.java)
                Log.d("Firestore", "${task.title} - ${task.progress}")
            }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error getting tasks", e)
        }
}


// =============================
// Update Task Status
// =============================
fun updateTaskStatus(taskId: String, newStatus: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("tasks").document(taskId)
        .update("status", newStatus)
        .addOnSuccessListener {
            Log.d("Firestore", "Task updated to $newStatus")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error updating task", e)
        }
}

// =============================
// Delete Task
// =============================
fun deleteTask(taskId: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("tasks").document(taskId)
        .delete()
        .addOnSuccessListener {
            Log.d("Firestore", "Task deleted")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error deleting task", e)
        }
}

// =============================
// Confirm Task
// =============================
fun confirmTask(taskId: String) {
    val db = FirebaseFirestore.getInstance()
    val taskRef = db.collection("tasks").document(taskId)

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

// =============================
// Dismiss Task
// =============================
fun dismissTask(taskId: String) {
    val db = FirebaseFirestore.getInstance()
    val taskRef = db.collection("tasks").document(taskId)

    db.runTransaction { transaction ->
        transaction.update(taskRef, mapOf(
            "status" to "Dismissed",
            "streak" to 0
        ))
    }.addOnSuccessListener {
        Log.d("Firestore", "✅ Task dismissed and streak reset")
    }.addOnFailureListener { e ->
        Log.w("Firestore", "❌ Error dismissing task", e)
    }
}