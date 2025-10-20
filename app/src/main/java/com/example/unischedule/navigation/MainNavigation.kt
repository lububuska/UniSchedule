package com.example.unischedule.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.unischedule.data.SettingsManager
import com.example.unischedule.ui.components.AddLessonDialog
import com.example.unischedule.ui.components.BottomBar
import com.example.unischedule.ui.screens.CalendarScreen
import com.example.unischedule.ui.screens.SettingsScreen
import com.example.unischedule.ui.screens.TodayLessonsScreen
import com.example.unischedule.data.Lesson

@Composable
fun MainNavigation(
    context: Context,
    settings: SettingsManager,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val navController = rememberNavController()
    var showAddLessonDialog by remember { mutableStateOf(false) }

    var refreshTrigger by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            BottomBar(
                navController = navController,
                onAddClick = { showAddLessonDialog = true }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "calendar",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("calendar") { CalendarScreen(navController, currentLang = currentLanguage) }

            composable("today") {
                TodayLessonsScreen(refreshTrigger = refreshTrigger)
            }

            composable("settings") {
                SettingsScreen(
                    settings = settings,
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    currentLanguage = currentLanguage,
                    onLanguageChange = onLanguageChange
                )
            }
        }
    }

    if (showAddLessonDialog) {
        AddLessonDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showAddLessonDialog = false },
            onLessonAdded = { lesson: Lesson ->
                showAddLessonDialog = false
                refreshTrigger++
            }
        )
    }
}
