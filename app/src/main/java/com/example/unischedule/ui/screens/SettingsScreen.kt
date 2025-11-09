package com.example.unischedule.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.unischedule.R
import com.example.unischedule.LoginActivity
import com.example.unischedule.data.SessionManager
import com.example.unischedule.data.SettingsManager
import com.example.unischedule.utils.LocaleUtils

@Composable
fun LocalizedText(
    stringResId: Int,
    currentLanguage: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSecondary
    )
) {
    val context = LocalContext.current
    val localizedContext = remember(currentLanguage) {
        LocaleUtils.setLocale(context, currentLanguage)
    }
    Text(
        text = localizedContext.getString(stringResId),
        modifier = modifier,
        style = style
    )
}

@Composable
fun SettingsScreen(
    settings: SettingsManager,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var darkTheme by remember { mutableStateOf(isDarkTheme) }
    var lang by remember { mutableStateOf(currentLanguage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        LocalizedText(
            stringResId = R.string.settings,
            currentLanguage = currentLanguage,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocalizedText(
                stringResId = R.string.dark_theme,
                currentLanguage = currentLanguage,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            Switch(
                checked = darkTheme,
                onCheckedChange = {
                    darkTheme = it
                    onThemeChange(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                    checkedTrackColor = MaterialTheme.colorScheme.onTertiary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary,
                    checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedBorderColor = MaterialTheme.colorScheme.tertiary
                )
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocalizedText(
                stringResId = R.string.language,
                currentLanguage = currentLanguage,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            Switch(
                checked = lang == "en",
                onCheckedChange = {
                    lang = if (it) "en" else "ru"
                    onLanguageChange(lang)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                    checkedTrackColor = MaterialTheme.colorScheme.onTertiary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary,
                    checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedBorderColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }

        Button(
            onClick = {
                val telegramUrl = "https://t.me/lybybyska00"
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = android.net.Uri.parse(telegramUrl)
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiary)
        ) {
            LocalizedText(
                stringResId = R.string.telegram_feedback,
                currentLanguage = currentLanguage,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                sessionManager.clearLogin()
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            LocalizedText(
                stringResId = R.string.logout,
                currentLanguage = currentLanguage,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }
}

