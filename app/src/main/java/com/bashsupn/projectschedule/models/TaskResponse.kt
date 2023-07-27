package com.bashsupn.projectschedule.models

data class TaskResponse(
    val `data`: Task,
    val message: String,
    val status: Boolean
)