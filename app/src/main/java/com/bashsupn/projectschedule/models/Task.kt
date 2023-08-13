package com.bashsupn.projectschedule.models

data class Task(
    val created_at: String,
    val dependencies: String,
    val description: Any,
    val duration: Int,
    val early_finish: Int,
    val early_start: Int,
    val end_date: Any,
    val id: Int,
    val job_id: Int,
    val late_finish: Int,
    val late_start: Int,
    val name: String,
    val slack: Int,
    val start_date: Any,
    val progress: Int,
    val status: String,
    val updated_at: String
)