package com.bashsupn.projectschedule.models

data class UsersResponse(
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val role: Role,
    val role_id: Int,
    val updated_at: String
)