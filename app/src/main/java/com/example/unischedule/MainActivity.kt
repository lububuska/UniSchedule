package com.example.unischedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.unischedule.ui.screens.LoginScreen
import com.example.unischedule.ui.theme.UniScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniScheduleTheme {
                LoginScreen(
                    onLoginClick = {
                        // TODO: логику перехода
                    }
                )
            }
        }
    }
}
