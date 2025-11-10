package com.example.unischedule.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.unischedule.R
import com.example.unischedule.data.Lesson
import com.example.unischedule.data.UserDatabaseHelper
import com.example.unischedule.utils.LocaleUtils
import com.example.unischedule.ui.components.TimePickerField
import com.example.unischedule.ui.components.OutlinedTextFieldLocalized

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onLessonAdded: (Lesson) -> Unit
) {
    val context = LocalContext.current
    val db = remember { UserDatabaseHelper(context) }

    var name by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var classroom by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("08:00") }
    var endTime by remember { mutableStateOf("08:00") }
    var selectedDayNumber by remember { mutableStateOf(1) }
    var isEvenWeek by remember { mutableStateOf(true) }

    val prefs = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val userId = prefs.getString("user_id", null)

    val localizedContext = remember(currentLanguage) {
        LocaleUtils.setLocale(context, currentLanguage)
    }

    val daysOfWeek = listOf(
        R.string.day_monday to 1,
        R.string.day_tuesday to 2,
        R.string.day_wednesday to 3,
        R.string.day_thursday to 4,
        R.string.day_friday to 5,
        R.string.day_saturday to 6,
        R.string.day_sunday to 7
    )

    val localizedDaysOfWeek = remember(currentLanguage) {
        daysOfWeek.map { (resId, number) ->
            localizedContext.getString(resId) to number
        }
    }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = localizedContext.getString(R.string.close),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                TextButton(onClick = {
                    if (name.isBlank() || startTime.isBlank() || endTime.isBlank()) {
                        Toast.makeText(
                            context,
                            localizedContext.getString(R.string.fill_required_fields),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    val startParts = startTime.split(":").map { it.toInt() }
                    val endParts = endTime.split(":").map { it.toInt() }

                    val startMinutes = startParts[0] * 60 + startParts[1]
                    val endMinutes = endParts[0] * 60 + endParts[1]

                    if (endMinutes <= startMinutes) {
                        Toast.makeText(
                            context,
                            localizedContext.getString(R.string.invalid_time_range),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    val lesson = Lesson(
                        id = 0,
                        name = name,
                        startTime = startTime,
                        endTime = endTime,
                        teacher = if (teacher.isBlank()) "—" else teacher,
                        classroom = if (classroom.isBlank()) "—" else classroom,
                        weekday = selectedDayNumber,
                        isEvenWeek = isEvenWeek,
                        userId = userId
                    )

                    db.addLesson(lesson)
                    Toast.makeText(
                        context,
                        localizedContext.getString(R.string.lesson_added),
                        Toast.LENGTH_SHORT
                    ).show()
                    onLessonAdded(lesson)
                    onDismiss()
                }) {
                    Text(
                        text = localizedContext.getString(R.string.add),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldLocalized(name, R.string.lesson_name, { name = it }, localizedContext)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextFieldLocalized(teacher, R.string.professor, { teacher = it }, localizedContext)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextFieldLocalized(classroom, R.string.auditorium, { classroom = it }, localizedContext)
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TimePickerField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(localizedContext.getString(R.string.start), style = MaterialTheme.typography.bodySmall) }
                )
                TimePickerField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(localizedContext.getString(R.string.end), style = MaterialTheme.typography.bodySmall) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                val selectedDayName =
                    localizedDaysOfWeek.first { it.second == selectedDayNumber }.first
                OutlinedTextField(
                    value = selectedDayName,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondary
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(15.dp),
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    localizedDaysOfWeek.forEach { (dayName, number) ->
                        DropdownMenuItem(
                            text = { Text(dayName) },
                            onClick = {
                                selectedDayNumber = number
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = localizedContext.getString(R.string.even_week),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Switch(
                    checked = isEvenWeek,
                    onCheckedChange = { isEvenWeek = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                        checkedTrackColor = MaterialTheme.colorScheme.onTertiary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
        }
    }
}

