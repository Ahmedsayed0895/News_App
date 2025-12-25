package com.task.newsapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.task.newsapp.data.dataSource.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val email = userPreferences.getUserEmail()

    fun logout() {
        userPreferences.logout()
    }
}