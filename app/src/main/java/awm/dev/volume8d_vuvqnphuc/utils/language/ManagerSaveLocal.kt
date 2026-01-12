package com.example.datatransfer.utils.language

import android.content.Context
import android.content.SharedPreferences
import awm.dev.volume8d_vuvqnphuc.data.local.LANG

object ManagerSaveLocal {
    private const val PREF_NAME = "gps_preferences"
    private const val KEY_LANGUAGE = "app_language"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setLanguageApp(lang: LANG) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, lang.name).apply()
    }

    fun getLanguageApp(): LANG? {
        val langString = sharedPreferences.getString(KEY_LANGUAGE, null) ?: return null
        return try {
            LANG.valueOf(langString)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}