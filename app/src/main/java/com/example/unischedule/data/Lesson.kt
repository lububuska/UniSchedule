package com.example.unischedule.data

data class Lesson(
    val id: Int = 0,
    val name: String,
    val startTime: String,
    val endTime: String,
    val teacher: String?,
    val classroom: String?,
    val weekday: Int,
    val isEvenWeek: Boolean
)
