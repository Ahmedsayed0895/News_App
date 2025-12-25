package com.task.newsapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.task.newsapp.ui.Routes
import com.task.newsapp.ui.theme.Gray
import com.task.newsapp.ui.theme.PrimaryBlue
import com.task.newsapp.ui.theme.White

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF3F4F6))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "user icon",
            modifier = Modifier.size(48.dp),
            tint = PrimaryBlue
        )

        Text(
            text = "Hello",
            style = MaterialTheme.typography.headlineLarge.copy(color = Gray),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "@${viewModel.email}",
            style = MaterialTheme.typography.labelSmall.copy(color = Gray),
            modifier = Modifier.padding(bottom = 8.dp)

        )

        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = White
            )
        ) {
            Text("Logout")
        }
    }
}