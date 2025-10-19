package com.example.unischedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.unischedule.data.SettingsManager
import com.example.unischedule.navigation.MainNavigation
import com.example.unischedule.ui.theme.UniScheduleTheme
import com.example.unischedule.utils.LocaleUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsManager(this)
        val startLang = settings.getLanguage()
        val startDark = settings.isDarkTheme()

        LocaleUtils.setLocale(this, startLang)

        setContent {
            var currentLang by remember { mutableStateOf(startLang) }
            var isDarkTheme by remember { mutableStateOf(startDark) }

            val localizedContext = remember(currentLang) {
                LocaleUtils.setLocale(this, currentLang)
            }

            UniScheduleTheme(darkTheme = isDarkTheme) {
                MainNavigation(
                    context = localizedContext,
                    settings = settings,
                    isDarkTheme = isDarkTheme,
                    onThemeChange = {
                        isDarkTheme = it
                        settings.setDarkTheme(it)
                    },
                    currentLanguage = currentLang,
                    onLanguageChange = { newLang ->
                        currentLang = newLang
                        settings.setLanguage(newLang)
                    }
                )
            }
        }
    }
}
