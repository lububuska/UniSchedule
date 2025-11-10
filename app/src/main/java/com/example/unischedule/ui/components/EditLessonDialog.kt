package com.example.unischedule.ui.components

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.unischedule.R
import com.example.unischedule.data.Lesson
import com.example.unischedule.data.UserDatabaseHelper
import com.example.unischedule.ui.theme.Grey
import java.util.*

fun Context.getLocalizedContext(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = android.content.res.Configuration()
    config.setLocale(locale)
    return createConfigurationContext(config)
}

@Composable
fun OutlinedTextFieldLocalized(
    value: String,
    placeholderRes: Int,
    onValueChange: (String) -> Unit,
    localizedContext: Context,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSecondary
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(15.dp),
        placeholder = {
            Text(
                text = localizedContext.getString(placeholderRes),
                style = textStyle,
                color = Grey
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSecondary
    )
) {
    val context = LocalContext.current

    fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val formatted = String.format("%02d:%02d", hour, minute)
                onTimeSelected(formatted)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showTimePicker(onValueChange) }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            textStyle = textStyle,
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.onPrimary,
                disabledTextColor = MaterialTheme.colorScheme.onSecondary,
                disabledLabelColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = RoundedCornerShape(15.dp),
            label = label
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLessonDialog(
    lesson: Lesson,
    currentLanguage: String,
    onDismiss: () -> Unit,
    onLessonUpdated: (Lesson) -> Unit
) {
    val context = LocalContext.current
    val db = remember { UserDatabaseHelper(context) }

    var name by remember { mutableStateOf(lesson.name) }
    var teacher by remember { mutableStateOf(lesson.teacher ?: "") }
    var classroom by remember { mutableStateOf(lesson.classroom ?: "") }
    var startTime by remember { mutableStateOf(lesson.startTime) }
    var endTime by remember { mutableStateOf(lesson.endTime) }
    var selectedDayNumber by remember { mutableStateOf(lesson.weekday) }
    var isEvenWeek by remember { mutableStateOf(lesson.isEvenWeek) }

    val localizedContext = remember(currentLanguage) {
        context.getLocalizedContext(currentLanguage)
    }

    val saveText = localizedContext.getString(R.string.save)
    val closeText = localizedContext.getString(R.string.close)
    val fillFieldsText = localizedContext.getString(R.string.fill_required_fields)
    val invalidTimeText = localizedContext.getString(R.string.invalid_time_range)
    val lessonUpdatedText = localizedContext.getString(R.string.lesson_updated)
    val startText = localizedContext.getString(R.string.start)
    val endText = localizedContext.getString(R.string.end)
    val evenWeekText = localizedContext.getString(R.string.even_week)
    val nameHint = localizedContext.getString(R.string.lesson_name)
    val teacherHint = localizedContext.getString(R.string.professor)
    val classroomHint = localizedContext.getString(R.string.auditorium)

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
        daysOfWeek.map { (resId, number) -> localizedContext.getString(resId) to number }
    }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = closeText,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                TextButton(onClick = {
                    if (name.isBlank() || startTime.isBlank() || endTime.isBlank()) {
                        Toast.makeText(context, fillFieldsText, Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    val startParts = startTime.split(":").map { it.toInt() }
                    val endParts = endTime.split(":").map { it.toInt() }
                    val startMinutes = startParts[0] * 60 + startParts[1]
                    val endMinutes = endParts[0] * 60 + endParts[1]

                    if (endMinutes <= startMinutes) {
                        Toast.makeText(context, invalidTimeText, Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    val updatedLesson = lesson.copy(
                        name = name,
                        startTime = startTime,
                        endTime = endTime,
                        teacher = if (teacher.isBlank()) "—" else teacher,
                        classroom = if (classroom.isBlank()) "—" else classroom,
                        weekday = selectedDayNumber,
                        isEvenWeek = isEvenWeek
                    )

                    db.updateLesson(updatedLesson)
                    Toast.makeText(context, lessonUpdatedText, Toast.LENGTH_SHORT).show()
                    onLessonUpdated(updatedLesson)
                    onDismiss()
                }) {
                    Text(text = saveText, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                    label = { Text(startText, style = MaterialTheme.typography.bodySmall) }
                )
                TimePickerField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(endText, style = MaterialTheme.typography.bodySmall) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                val selectedDayName = localizedDaysOfWeek.first { it.second == selectedDayNumber }.first
                OutlinedTextField(
                    value = selectedDayName,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSecondary),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(15.dp),
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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
                Text(text = evenWeekText, color = MaterialTheme.colorScheme.onPrimary)
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    db.deleteLesson(lesson.id)
                    Toast.makeText(context, localizedContext.getString(R.string.lesson_deleted), Toast.LENGTH_SHORT).show()
                    onLessonUpdated(lesson.copy(id = -1))
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = localizedContext.getString(R.string.delete),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }
    }
}
