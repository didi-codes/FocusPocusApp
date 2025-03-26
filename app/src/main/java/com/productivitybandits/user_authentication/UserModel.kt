package com.productivitybandits.user_authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


///// Register New User /////
fun registerUser(user:User, onResult: (Boolean, String?) -> Unit){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(user.email, user.password)
        .addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                val newUser = user.copy(uid = uid)

                db.collection("users").document(uid).set(newUser)
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



