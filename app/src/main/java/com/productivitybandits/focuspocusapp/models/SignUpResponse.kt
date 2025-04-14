package com.productivitybandits.focuspocusapp.models

data class SignUpResponse (
    val success: Boolean,
    val message: String
)

// Data class for receiving signup response from backend
// Can be expanded if backend returns user details or auth token on signup
