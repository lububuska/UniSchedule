package com.example.unischedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.unischedule.data.SettingsManager
import com.example.unischedule.ui.screens.EnterScreen
import com.example.unischedule.ui.theme.UniScheduleTheme

class EnterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsManager(this)

        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            UniScheduleTheme(darkTheme = isDarkTheme) {
                EnterScreen(
                    onLoginClick = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    },
                    darkTheme = isDarkTheme
                )
            }
        }
    }
}

