package com.task.newsapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val user = authRepository.loginUser(email, password)
                if (user != null) {
                    _loginState.value = LoginState.Success("Login Successful")
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