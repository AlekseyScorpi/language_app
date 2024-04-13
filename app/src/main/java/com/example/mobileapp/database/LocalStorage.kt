package com.example.mobileapp.database

import android.content.Context
import com.example.mobileapp.LanguageApplication

class LocalStorage(private val application: LanguageApplication) {
    private val sharedPref = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String {
        return sharedPref.getString(key, null) ?: ""
    }

    fun saveInt(key: String, value: Int) {
        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, 0)
    }
}