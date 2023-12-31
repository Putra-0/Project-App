package com.bashsupn.projectschedule.models

data class Projects(
    val address: String,
    val client_name: String,
    val created_at: String,
    val description: Any,
    val end_date: Any,
    val id: Int,
    val name: String,
    val start_date: Any,
    val progress : Int,
    val status: String,
    val updated_at: String,
    val user_id: Int,
    val user: Login
)