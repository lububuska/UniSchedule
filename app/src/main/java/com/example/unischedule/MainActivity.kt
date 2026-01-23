package com.example.unischedule

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.unischedule.data.SettingsManager
import com.example.unischedule.navigation.MainNavigation
import com.example.unischedule.ui.theme.UniScheduleTheme
import com.example.unischedule.utils.LocaleUtils

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var onLightChanged: ((Boolean) -> Unit)? = null
    private var isAutoThemeEnabled = false

    companion object {
        private const val LIGHT_THRESHOLD = 50f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val settings = SettingsManager(this)
        val startLang = settings.getLanguage()
        val startDark = settings.isDarkTheme()
        val startAutoTheme = settings.isAutoTheme()

        LocaleUtils.setLocale(this, startLang)

        setContent {
            var currentLang by remember { mutableStateOf(startLang) }
            var isDarkTheme by remember { mutableStateOf(startDark) }
            var isAutoTheme by remember { mutableStateOf(startAutoTheme) }

            LaunchedEffect(isAutoTheme) {
                isAutoThemeEnabled = isAutoTheme
            }

            DisposableEffect(Unit) {
                onLightChanged = { shouldBeDark ->
                    if (isAutoThemeEnabled) {
                        isDarkTheme = shouldBeDark
                    }
                }
                onDispose {
                    onLightChanged = null
                }
            }

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
                    isAutoTheme = isAutoTheme,
                    onAutoThemeChange = {
                        isAutoTheme = it
                        settings.setAutoTheme(it)
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

    override fun onResume() {
        super.onResume()
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]
            val shouldBeDark = lux < LIGHT_THRESHOLD
            onLightChanged?.invoke(shouldBeDark)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for light sensor
    }
}
