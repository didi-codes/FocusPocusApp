package com.productivitybandits.user_authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


///// Register New User /////
fun registerUser(
    email: String,
    password: String,
    username: String,
    firstName: String? = null,
    lastName: String? = null,
    onResult: (Boolean, String?) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // First, check if a user with the same email or username already exists
    db.collection("users")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { emailResult ->
            if (!emailResult.isEmpty) {
                onResult(false, "An account with this email already exists.")
                return@addOnSuccessListener
            }

            // Then check if username is already taken
            db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { usernameResult ->
                    if (!usernameResult.isEmpty) {
                        onResult(false, "Username is already taken.")
                        return@addOnSuccessListener
                    }

                    // If email and username are unique, create the user
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.e("FirebaseAuth", "Registration failed: ${task.exception?.message}")
                                onResult(false, task.exception?.message ?: "Unknown error")
                                return@addOnCompleteListener
                            }

                            val uid = task.result?.user?.uid
                            if (uid == null) {
                                Log.e("FirebaseAuth", "UID is null")
                                onResult(false, "UID is null")
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
                                    Log.d("FirebaseAuth", "User registered and stored in Firestore.")
                                    onResult(true, uid)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseAuth", "Error storing user data", e)
                                    onResult(false, e.message)
                                }
                        }
                }
                .addOnFailureListener { e ->
                    onResult(false, "Failed to check username: ${e.message}")
                }
        }
        .addOnFailureListener { e ->
            onResult(false, "Failed to check email: ${e.message}")
        }
}

//fun registerUser(
//    email: String,
//    password: String,
//    username: String,
//    firstName: String? = null,
//    lastName: String? = null,
//    onResult: (Boolean, String?) -> Unit
//) {
//    val auth = FirebaseAuth.getInstance()
//    val db = FirebaseFirestore.getInstance()
//
//    auth.createUserWithEmailAndPassword(email, password)
//        .addOnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.e("FirebaseAuth", "Registration failed: ${task.exception?.message}")
//                onResult(false, task.exception?.message ?: "Unknown error")
//                return@addOnCompleteListener
//            }
//
//            val uid = task.result?.user?.uid
//            if (uid == null) {
//                Log.e("FirebaseAuth", "UID is null")
//                onResult(false, "UID is null")
//                return@addOnCompleteListener
//            }
//
//            val user = User(
//                uid = uid,
//                email = email,
//                username = username,
//                firstName = "",
//                lastName = ""
//            )
//
//            db.collection("users").document(uid).set(user)
//                .addOnSuccessListener {
//                    Log.d("FirebaseAuth", "User registered and stored in Firestore.")
//                    onResult(true, uid)
//                }
//                .addOnFailureListener { e ->
//                    Log.e("FirebaseAuth", "Error storing user data", e)
//                    onResult(false, e.message)
//                }
//        }
//}




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





