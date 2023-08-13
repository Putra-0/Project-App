package com.bashsupn.projectschedule.models

data class Project(
    val address: String,
    val client_name: String,
    val created_at: String,
    val description: Any,
    val end_date: String,
    val id: Int,
    val name: String,
    val progress: Int,
    val start_date: String,
    val status: String,
    val type: Type,
    val type_id: Int,
    val updated_at: String,
    val user: Login,
    val user_id: Int
)
