package com.example.unischedule.data

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "is_dark_theme"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_AUTO_THEME = "is_auto_theme"
    }

    fun isDarkTheme(): Boolean = prefs.getBoolean(KEY_THEME, false)

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_THEME, enabled).apply()
    }

    fun isAutoTheme(): Boolean = prefs.getBoolean(KEY_AUTO_THEME, false)

    fun setAutoTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_THEME, enabled).apply()
    }

    fun getLanguage(): String = prefs.getString(KEY_LANGUAGE, "ru") ?: "ru"

    fun setLanguage(lang: String) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
    }
}
