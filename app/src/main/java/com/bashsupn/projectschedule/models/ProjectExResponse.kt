package com.bashsupn.projectschedule.models

data class ProjectExResponse(
    val `data`: List<Project>,
    val message: String,
    val status: Boolean
)