package com.productivitybandits.focuspocusapp.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLoginState(isLoggedIn: Boolean) {
        prefs.edit().putBoolean("IS_LOGGED_IN", isLoggedIn).apply()
    }

    fun getLoginState(): Boolean {
        return prefs.getBoolean("IS_LOGGED_IN", false)
    }

    fun saveUser(username: String, password: String) {
        prefs.edit().putString("USER_$username", password).apply() // Store passsword for the username
        saveLoginState(true)   // Automatically Logs in after sign-up
    }

    fun validateUser(username: String, password: String): Boolean {
        return prefs.getString("USER_$username", null) == password
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun saveEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return prefs.getString("email", null)
    }
}