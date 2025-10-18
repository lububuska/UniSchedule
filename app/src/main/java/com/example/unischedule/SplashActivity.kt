package com.example.unischedule

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.unischedule.data.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Устанавливаем SplashScreen через API
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)

        // Можно добавить небольшую задержку для показа анимации сплэш
        lifecycleScope.launch {
            delay(1000)

            val nextActivity = if (session.isLoggedIn()) {
                MainActivity::class.java  // пользователь авторизован
            } else {
                EnterActivity::class.java // пользователь не авторизован
            }

            startActivity(Intent(this@SplashActivity, nextActivity))
            finish() // закрываем SplashActivity
        }
    }
}
