package com.task.newsapp.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import com.task.newsapp.ui.component.AppTextField
import com.task.newsapp.ui.theme.Gray
import com.task.newsapp.ui.theme.LightGray
import com.task.newsapp.ui.theme.NewsAppTheme
import com.task.newsapp.ui.theme.PrimaryBlue
import com.task.newsapp.ui.theme.White
import com.task.newsapp.ui.util.DevicePosture

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(state) {
        when (state) {
            is LoginState.Success -> {
                onLoginSuccess()
                viewModel.restState()
            }

            is LoginState.Error -> Toast.makeText(
                context,
                (state as LoginState.Error).message,
                Toast.LENGTH_LONG
            ).show()

            else -> Unit
        }
    }
    LoginScreenContent(
        state = state,
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = viewModel::login,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (username: String, password: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DevicePosture.fromWindowSizeClass(windowSizeClass)
    when (deviceConfiguration) {
        DevicePosture.MOBILE_PORTRAIT,
        DevicePosture.TABLET_PORTRAIT -> {
            Column(
                modifier = Modifier
                    .background(LightGray)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Logo",
                )
                Text(
                    text = "Welcome Back", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                )
                Text(
                    text = "Please enter your details to sign in.",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Gray
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppTextField(
                    value = email.trim().lowercase(),
                    onValueChange = onEmailChange,
                    placeholderText = "Enter your Email",
                    leadingIcon = Icons.Default.Email,
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholderText = "Enter your Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (state is LoginState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { onLoginClick(email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = White,
                            disabledContainerColor = Gray,
                            disabledContentColor = LightGray

                        ),
                        enabled = email.isNotEmpty() && password.isNotEmpty()
                    ) {
                        Text("Login")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Gray
                                )
                            ) {
                                append("Don't have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                            ) {
                                append("Register")
                            }
                        }
                    )
                }
            }
        }

        DevicePosture.MOBILE_LANDSCAPE,
        DevicePosture.TABLET_LANDSCAPE,
        DevicePosture.BIG_SCREEN -> {
            Row(
                modifier = Modifier
                    .background(LightGray)
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "App Logo",
                    )
                    Text(
                        text = "Welcome Back", style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    )
                    Text(
                        text = "Please enter your details to sign in.",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Gray
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

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
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    if (state is LoginState.Loading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = { onLoginClick(email, password) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue,
                                contentColor = White,
                                disabledContainerColor = Gray,
                                disabledContentColor = LightGray

                            ),
                            enabled = email.isNotEmpty() && password.isNotEmpty()
                        ) {
                            Text("Login")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Gray
                                    )
                                ) {
                                    append("Don't have an account? ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlue
                                    )
                                ) {
                                    append("Register")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NewsAppTheme {
        LoginScreenContent(
            state = LoginState.Idle,
            email = "asdf",
            password = "asdf",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = { _, _ -> },
            onNavigateToRegister = {}
        )
    }

}