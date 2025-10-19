package com.example.unischedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.unischedule.data.SessionManager
import com.example.unischedule.ui.screens.RegisterScreen
import com.example.unischedule.ui.theme.UniScheduleTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = SessionManager(this)

        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            UniScheduleTheme {
                RegisterScreen(
                    onRegisterSuccess = {
                        session.setLoggedIn(true)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    },
                    onBack = { finish() },
                    darkTheme = isDarkTheme
                )
            }
        }
    }
}

