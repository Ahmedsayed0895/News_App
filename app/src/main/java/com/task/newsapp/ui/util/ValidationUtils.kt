package com.task.newsapp.ui.util

import android.util.Patterns

object ValidationUtils {
    fun isValidUserName(userName: String): Boolean = userName.isNotBlank()

    fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(password: String): Boolean = password.length >= 6 && password.isNotBlank()

}