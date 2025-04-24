package com.productivitybandits.taskmanager

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

// =============================
// Fetch All Tasks (under user)
// =============================
fun fetchTasks(userId: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users")
        .document(userId)
        .collection("tasks")
        .get()
        .addOnSuccessListener { result ->
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
fun updateTaskStatus(userId: String, taskId: String, newStatus: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .document(userId)
        .collection("tasks")
        .document(taskId)
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

// =============================
// Confirm Task (increment streak)
// =============================
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

// =============================
// Dismiss Task (reset streak)
// =============================
fun dismissTask(userId: String, taskId: String) {
    val db = FirebaseFirestore.getInstance()
    val taskRef = db.collection("users").document(userId)
        .collection("tasks").document(taskId)

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
