package com.bashsupn.projectschedule.models

data class ProjectsResponse(
    val `data`: List<Projects>,
    val message: String,
    val status: Boolean
)