package com.example.mobileapp

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import java.util.*

fun setLocale(language: String, context: Context) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)

    saveLanguagePreference(language, context) // Сохранение языка в SharedPreferences
}

fun saveLanguagePreference(language: String, context: Context) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.putString("language", language)
    editor.apply()
}