package com.example.cerviewja.data.local

import android.content.Context
import android.content.SharedPreferences

class UserSharedPreferences(
    context: Context
) {

    private val PRIVATE_MODE = 0
    private val PREF_NAME = "user-prefs"
    private var sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun saveValue(key: String, value: String?) {
        sharedPrefs.edit()
            .putString(key, value)
            .apply()
    }

    fun loadValue(key: String): String {
        return sharedPrefs.getString(key, "").toString()
    }

    fun isLogged(key: String): Boolean {
        return loadValue(key) != ""
    }

    fun cleanValue(key: String) {
        sharedPrefs.edit()
            .remove(key)
            .apply()
    }
}