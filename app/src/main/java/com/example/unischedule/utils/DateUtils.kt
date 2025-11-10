package com.example.unischedule.utils

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    fun getLocalizedDayName(date: LocalDate): String {
        val locale = Locale.getDefault()
        val day = date.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        return day.replaceFirstChar { it.uppercase() }
    }

    fun getLocalizedDateWithMonth(date: LocalDate): String {
        val locale = Locale.getDefault()
        return when (locale.language) {
            "ru" -> {
                val monthNames = listOf(
                    "января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"
                )
                val monthGenitive = monthNames[date.monthValue - 1]
                "${date.dayOfMonth} $monthGenitive"
            }
            else -> {
                val monthName = date.month.getDisplayName(TextStyle.FULL, locale)
                "${date.dayOfMonth} $monthName"
            }
        }
    }
}