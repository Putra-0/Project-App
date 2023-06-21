package com.bashsupn.projectschedule.models

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String,
    val user: Login
)