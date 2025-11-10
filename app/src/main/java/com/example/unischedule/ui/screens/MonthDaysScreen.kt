package com.example.unischedule.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.unischedule.R
import com.example.unischedule.utils.LocaleUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year

@Composable
fun MonthDaysScreen(
    navController: androidx.navigation.NavController,
    monthName: String,
    currentLanguage: String
) {
    val context = LocalContext.current
    val localizedContext = remember(currentLanguage) {
        LocaleUtils.setLocale(context, currentLanguage)
    }

    val year = LocalDate.now().year

    val monthStrings = listOf(
        localizedContext.getString(R.string.month_january),
        localizedContext.getString(R.string.month_february),
        localizedContext.getString(R.string.month_march),
        localizedContext.getString(R.string.month_april),
        localizedContext.getString(R.string.month_may),
        localizedContext.getString(R.string.month_june),
        localizedContext.getString(R.string.month_july),
        localizedContext.getString(R.string.month_august),
        localizedContext.getString(R.string.month_september),
        localizedContext.getString(R.string.month_october),
        localizedContext.getString(R.string.month_november),
        localizedContext.getString(R.string.month_december)
    )

    val weekDays = listOf(
        localizedContext.getString(R.string.day_monday_short),
        localizedContext.getString(R.string.day_tuesday_short),
        localizedContext.getString(R.string.day_wednesday_short),
        localizedContext.getString(R.string.day_thursday_short),
        localizedContext.getString(R.string.day_friday_short),
        localizedContext.getString(R.string.day_saturday_short),
        localizedContext.getString(R.string.day_sunday_short)
    )

    val monthIndex = monthStrings.indexOfFirst { it.equals(monthName, ignoreCase = true) }
    val month = if (monthIndex != -1) Month.of(monthIndex + 1) else LocalDate.now().month

    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = month.length(Year.isLeap(year.toLong()))
    val startDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7
    val days = (1..daysInMonth).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        val monthDisplay = monthStrings[month.value - 1]
        Text(
            text = "$monthDisplay $year",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekDays.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.width(40.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            items(startDayOfWeek) {
                Box(modifier = Modifier.size(40.dp))
            }

            items(days) { day ->
                val dayOfWeek = LocalDate.of(year, month, day).dayOfWeek
                val isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY

                val backgroundColor = if (isWeekend)
                    MaterialTheme.colorScheme.onTertiary
                else
                    MaterialTheme.colorScheme.tertiary

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current
                        ) {
                            val selectedDate = LocalDate.of(year, month, day)
                            navController.navigate("day/${selectedDate}")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
