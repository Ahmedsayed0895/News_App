package com.task.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.task.newsapp.data.dataSource.local.UserPreferences
import com.task.newsapp.ui.AppNavigation
import com.task.newsapp.ui.Routes
import com.task.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userPreferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                AppNavigation(
                    startDestination =
                        if (userPreferences.isLoggedIn()) Routes.HOME
                        else Routes.LOGIN
                )
            }
        }
    }
}
