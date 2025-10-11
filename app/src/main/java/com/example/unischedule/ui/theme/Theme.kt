package com.example.unischedule.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Tiffany,
    onPrimary = White,
    secondary = Lillac,
    onSecondary = Black
)

private val DarkColors = darkColorScheme(
    primary = Tiffany,
    onPrimary = White,
    secondary = Lillac,
    onSecondary = Black
)

@Composable
fun UniScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
