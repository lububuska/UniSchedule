package com.example.unischedule.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.unischedule.data.SettingsManager
import com.example.unischedule.ui.components.BottomBar
import com.example.unischedule.ui.screens.CalendarScreen
import com.example.unischedule.ui.screens.SettingsScreen

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "calendar",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("calendar") { CalendarScreen(navController, currentLang = currentLanguage) }

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
}
