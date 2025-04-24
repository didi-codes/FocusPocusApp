package com.productivitybandits.user_authentication

data class User(
    val uid: String="",
    val firstName: String="",
    val lastName: String="",
    val email: String="",
    val username: String = "",
    val photoUrl: String = ""
)