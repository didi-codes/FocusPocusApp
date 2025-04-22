package com.productivitybandits.focuspocusapp.models

data class SignUpRequest (
    val username: String,
    val email: String,
    val password: String
)

// Data class sent as the request body for the signup API call
// Backend must expect and parse these fields correctly
