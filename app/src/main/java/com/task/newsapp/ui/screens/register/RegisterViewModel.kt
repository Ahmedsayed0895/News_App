package com.task.newsapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.data.model.User
import com.task.newsapp.domain.repository.AuthRepository
import com.task.newsapp.ui.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    fun registerUser(user: User) {
        if (!ValidationUtils.isValidUserName(user.name)) {
            _registerState.value = RegisterState.Error("Please enter a valid username")
            return
        }
        if (!ValidationUtils.isValidEmail(user.email)) {
            _registerState.value = RegisterState.Error("Invalid email address format")
            return
        }
        if (!ValidationUtils.isValidPassword(user.password)) {
            _registerState.value = RegisterState.Error("Password must be at least 6 characters")
            return
        }


        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val existingUser = authRepository.getUserByEmail(user.email)
                if (existingUser != null) {
                    _registerState.value = RegisterState.Error("User already exists")
                } else {
                    authRepository.registerUser(user)
                    _registerState.value = RegisterState.Success("Registration Successful")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "An Error Occurred")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }

}