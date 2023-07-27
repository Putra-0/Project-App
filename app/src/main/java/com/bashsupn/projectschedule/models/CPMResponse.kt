package com.bashsupn.projectschedule.models

data class CPMResponse(
    val critical_path: List<String>,
    val total_duration_critical_path: Int
)