package com.productivitybandits.focuspocusapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productivitybandits.focuspocusapp.repository.AuthRepository
import com.productivitybandits.focuspocusapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel(

    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    val isAuthenticated: StateFlow<Boolean> = repository.isAuthenticated

    // Function to Log in
    suspend fun login(username: String, password: String): Boolean {
        return repository.login(username, password).also { success ->
            if (success) {
                sessionManager.saveLoginState(true)
            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            sessionManager.clearSession()
        }
    }

}


