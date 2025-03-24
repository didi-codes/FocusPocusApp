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
        return prefs.getBoolean("IS_LOGGED_in", false)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}