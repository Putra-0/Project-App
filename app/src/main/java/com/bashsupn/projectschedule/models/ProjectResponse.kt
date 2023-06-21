package com.bashsupn.projectschedule.models

data class ProjectResponse(
    val data: Project,
    val message: String,
    val status: Boolean,
)
