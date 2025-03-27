package com.productivitybandits.user_authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


///// Register New User /////
fun registerUser(email: String, password: String, username: String, firstname: String, lastName: String, onResult: (Boolean, String?) -> Unit){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                val user = mapOf(
                    "uid" to uid,
                    "firstName" to firstname,
                    "lastname" to lastName,
                    "email" to email,
                    "password" to password,
                    "username" to username,
                )
                db.collection("users").document(uid).set(user)
                    .addOnSuccessListener {
                        Log.d("FirebaseAuth", "User registered and stored in firestore ")
                        onResult(true, uid)
                    }
                    .addOnFailureListener{ e ->
                        Log.e("FirebaseAuth", "Error storing user data", e)
                        onResult(false, e.message)
                    }
            } else {
                Log.e("FirebaseAuth", "Registration Failed", task.exception)
                onResult(false, task.exception?.message)
            }

        }
}



//// Existing User Login ////
fun loginUser(email: String, password: String, onResult: (User?) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val user = document.toObject(User::class.java)
                            onResult(user)
                        } else {
                            onResult(null)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error fetching user", e)
                        onResult(null)
                    }
            } else {
                Log.e("Auth", "Login failed", task.exception)
                onResult(null)
            }
        }
}

//// Utilities ////
fun getUser(uid: String, onResult:(User?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(uid).get()
        .addOnSuccessListener { document ->
            if(document.exists()) {
                val user = document.toObject(User::class.java)
                onResult(user)
            } else {
                onResult(null)
            }

        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error fetching user", e)
            onResult(null)
        }
}





