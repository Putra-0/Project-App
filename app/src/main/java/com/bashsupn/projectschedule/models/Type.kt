package com.bashsupn.projectschedule.models

data class Type(
    val created_at: String,
    val id: Int,
    val name: String,
    val tasks: List<Task>,
    val updated_at: String
)