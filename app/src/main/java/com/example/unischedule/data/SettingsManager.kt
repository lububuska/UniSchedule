package com.example.unischedule.data

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "is_dark_theme"
        private const val KEY_LANGUAGE = "language"
    }

    fun isDarkTheme(): Boolean = prefs.getBoolean(KEY_THEME, false)

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_THEME, enabled).apply()
    }

    fun getLanguage(): String = prefs.getString(KEY_LANGUAGE, "ru") ?: "ru"

    fun setLanguage(lang: String) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
    }
}
