package com.example.unischedule.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unischedule.R
import com.example.unischedule.data.UserDatabaseHelper
import com.example.unischedule.data.Lesson
import com.example.unischedule.utils.DateUtils
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun TodayLessonsScreen(refreshTrigger: Int, localizedContext: android.content.Context) {
    val context = LocalContext.current
    val db = remember { UserDatabaseHelper(context) }

    val today = LocalDate.now()
    val weekday = today.dayOfWeek.value
    val weekNumber = today.get(WeekFields.of(Locale.getDefault()).weekOfYear())
    val isEvenWeek = weekNumber % 2 == 0

    val prefs = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val userId = prefs.getString("user_id", null)

    val lessons by remember(refreshTrigger) {
        mutableStateOf(
            if (userId != null) db.getLessonsForDay(weekday, isEvenWeek, userId) else emptyList()
        )
    }

    val dayName = DateUtils.getLocalizedDayName(today)
    val formattedDate = DateUtils.getLocalizedDateWithMonth(today)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (lessons.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    localizedContext.getString(R.string.no_lessons_today),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(lessons) { lesson ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "${lesson.startTime} — ${lesson.endTime}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                lesson.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            val classroomText = lesson.classroom?.let { "Ауд.$it" } ?: ""
                            val teacherText = lesson.teacher ?: ""
                            val combinedText = listOf(classroomText, teacherText)
                                .filter { it.isNotBlank() }
                                .joinToString(" ")

                            if (combinedText.isNotEmpty()) {
                                Text(
                                    combinedText,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
