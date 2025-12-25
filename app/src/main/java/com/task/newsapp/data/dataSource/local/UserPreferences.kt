package com.task.newsapp.data.dataSource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_EMAIL = "username"
    }
    fun saveEmail(email: String) {
        prefs.edit {
            putBoolean(KEY_IS_LOGGED_IN, true)
                .putString(KEY_EMAIL, email)
        }
    }
    fun getUserEmail(): String {
        return prefs.getString(KEY_EMAIL, "@USER") ?: "@USER"
    }
    fun saveLoginState(isLoggedIn: Boolean) {
        prefs.edit { putBoolean(KEY_IS_LOGGED_IN, isLoggedIn) }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit { clear() }
    }
}