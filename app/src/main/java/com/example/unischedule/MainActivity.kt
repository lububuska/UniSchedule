package com.example.unischedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.unischedule.ui.screens.EnterScreen
import com.example.unischedule.ui.screens.LoginScreen
import com.example.unischedule.ui.screens.RegisterScreen
import com.example.unischedule.ui.theme.UniScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniScheduleTheme {
                var currentScreen by remember { mutableStateOf("enter") }

                when (currentScreen) {
                    "enter" -> EnterScreen(
                        onLoginClick = { currentScreen = "login" }
                    )

                    "login" -> LoginScreen(
                        onLoginSuccess = {
                            // TODO: позже добавим переход к расписанию
                        },
                        onNavigateToRegister = { currentScreen = "register" },
                        onBack = { currentScreen = "enter" }
                    )

                    "register" -> RegisterScreen(
                        onRegisterSuccess = {
                            // TODO: позже добавим переход к расписанию
                        },
                        onBack = { currentScreen = "login" }
                    )
                }
            }
        }
    }
}
