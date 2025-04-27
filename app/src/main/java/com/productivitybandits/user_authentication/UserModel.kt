package com.productivitybandits.user_authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


///// Register New User /////

// Variables passed into method
fun registerUser(
    email: String,
    password: String,
    username: String,
    firstName: String? = null,
    lastName: String? = null,
    onResult: (Boolean, String?) -> Unit
) {
    // Variable to call auth instance
    val auth = FirebaseAuth.getInstance()
    //Variable to call db instance
    val db = FirebaseFirestore.getInstance()

    //
    db.collection("users").whereEqualTo("email", email).get()
        .addOnSuccessListener { emailResult ->
            if (!emailResult.isEmpty) {
                onResult(false, "An account with this email already exists.")
                return@addOnSuccessListener
            }

            db.collection("users").whereEqualTo("username", username).get()
                .addOnSuccessListener { usernameResult ->
                    if (!usernameResult.isEmpty) {
                        onResult(false, "Username is already taken.")
                        return@addOnSuccessListener
                    }

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                onResult(false, task.exception?.message ?: "Registration failed.")
                                return@addOnCompleteListener
                            }

                            val uid = task.result?.user?.uid
                            if (uid == null) {
                                onResult(false, "Registration failed: UID is null.")
                                return@addOnCompleteListener
                            }

                            val user = User(
                                uid = uid,
                                email = email,
                                username = username,
                                firstName = firstName ?: "",
                                lastName = lastName ?: ""
                            )

                            db.collection("users").document(uid).set(user)
                                .addOnSuccessListener {
                                    onResult(true, null) // ✅ Don't pass UID as a message
                                }
                                .addOnFailureListener { e ->
                                    onResult(false, "Error saving user data: ${e.message}")
                                }
                        }
                }
                .addOnFailureListener { e ->
                    onResult(false, "Error checking username: ${e.message}")
                }
        }
        .addOnFailureListener { e ->
            onResult(false, "Error checking email: ${e.message}")
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
//fun getUser(uid: String, onResult:(User?) -> Unit) {
//    val db = FirebaseFirestore.getInstance()
//    db.collection("users").document(uid).get()
//        .addOnSuccessListener { document ->
//            if(document.exists()) {
//                val user = document.toObject(User::class.java)
//                onResult(user)
//            } else {
//                onResult(null)
//            }
//
//        }
//        .addOnFailureListener { e ->
//            Log.e("Firestore", "Error fetching user", e)
//            onResult(null)
//        }
//}