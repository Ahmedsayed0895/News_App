package com.task.newsapp.ui.screens.register


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.task.newsapp.R
import com.task.newsapp.data.model.User
import com.task.newsapp.ui.component.AppTextField
import com.task.newsapp.ui.theme.Gray
import com.task.newsapp.ui.theme.LightGray
import com.task.newsapp.ui.theme.NewsAppTheme
import com.task.newsapp.ui.theme.PrimaryBlue
import com.task.newsapp.ui.theme.White

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.registerState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(state) {
        when (state) {
            is RegisterState.Success -> {
                onRegisterSuccess()
                viewModel.resetState()
            }

            is RegisterState.Error -> Toast.makeText(
                context,
                (state as RegisterState.Error).message,
                Toast.LENGTH_LONG
            ).show()

            else -> Unit
        }
    }
    RegisterScreenContent(
        state = state,
        email = email,
        password = password,
        username = userName,
        onUsernameChange = { userName = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onRegisterClick = viewModel::registerUser,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterScreenContent(
    state: RegisterState,
    email: String,
    password: String,
    username: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: (user: User) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(LightGray)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
        )
        Text(
            text = "Create Account", style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "Sign up to get the latest news updates.",
            style = MaterialTheme.typography.labelLarge.copy(
                color = Gray
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
        AppTextField(
            value = username,
            onValueChange = onUsernameChange,
            placeholderText = "Enter your Name",
            leadingIcon = Icons.Default.Person,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppTextField(
            value = email.trim().lowercase(),
            onValueChange = onEmailChange,
            placeholderText = "Enter your Email",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholderText = "Enter your Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state is RegisterState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    onRegisterClick(
                        User(
                            name = username,
                            email = email,
                            password = password
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = White
                ),
                enabled = email.isNotEmpty()
                        && password.isNotEmpty()
                        && username.isNotEmpty()
            ) {
                Text("Register")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToLogin) {
            Text(
                buildAnnotatedString {
                    append("Already registered? ? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    ) {
                        append("Login")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    NewsAppTheme {
        RegisterScreenContent(
            state = RegisterState.Idle,
            email = "asdf",
            password = "asdf",
            onEmailChange = {},
            onPasswordChange = {},
            onRegisterClick = { },
            onNavigateToLogin = {},
            username = "asdf",
            onUsernameChange = {}
        )
    }

}