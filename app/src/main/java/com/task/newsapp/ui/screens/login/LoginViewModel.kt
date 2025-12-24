package com.task.newsapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.data.dataSource.local.UserPreferences
import com.task.newsapp.domain.repository.AuthRepository
import com.task.newsapp.ui.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        if (!ValidationUtils.isValidEmail(email)) {
            _loginState.value = LoginState.Error("Invalid email address")
            return
        }

        if (password.isBlank()) {
            _loginState.value = LoginState.Error("Please enter your password")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val user = authRepository.loginUser(email, password)
                if (user != null) {
                    _loginState.value = LoginState.Success("Login Successful")
                    userPreferences.saveLoginState(true)
                } else {
                    _loginState.value = LoginState.Error("Invalid Credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "An Error Occurred")
            }
        }
    }

    fun restState() {
        _loginState.value = LoginState.Idle
    }
}