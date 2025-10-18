package com.example.unischedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.unischedule.ui.screens.EnterScreen
import com.example.unischedule.ui.theme.UniScheduleTheme

class EnterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniScheduleTheme {
                EnterScreen(
                    onLoginClick = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
