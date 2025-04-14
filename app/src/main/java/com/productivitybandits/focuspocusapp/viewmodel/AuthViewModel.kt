package com.productivitybandits.focuspocusapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productivitybandits.focuspocusapp.repository.AuthRepository
import com.productivitybandits.focuspocusapp.utils.SessionManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// ViewModel that handles user login/signup logic and state management
// Calls repository functions and manages session state


class AuthViewModel(

    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    val isAuthenticated: StateFlow<Boolean> = repository.isAuthenticated

    // ✅ LOGIN FUNCTION
    suspend fun login(username: String, password: String): Boolean {
        return repository.login(username, password).also { success ->
            if (success) {
                sessionManager.saveLoginState(true)
            }
        }
    }

    // ✅ SIGNUP FUNCTION
    suspend fun signUp(username: String, email: String, password: String): Boolean {
        return repository.signUp(username, email, password)
    }

    // ✅ LOGOUT FUNCTION
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            sessionManager.clearSession()
        }
    }

}


