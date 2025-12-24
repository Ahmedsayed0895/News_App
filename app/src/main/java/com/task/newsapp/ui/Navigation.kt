package com.task.newsapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.task.newsapp.ui.screens.articleDetails.ArticleDetailScreen
import com.task.newsapp.ui.screens.home.HomeScreen
import com.task.newsapp.ui.screens.login.LoginScreen
import com.task.newsapp.ui.screens.register.RegisterScreen

object Routs {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val DETAILS = "details"
    const val Favorite = "favorite"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routs.HOME) {
        composable(Routs.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routs.HOME)
                },
                onNavigateToRegister = {
                    navController.navigate(Routs.REGISTER)
                }
            )
        }

        composable(Routs.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routs.HOME)
                },
                onNavigateToLogin = {
                    navController.navigate(Routs.LOGIN)
                }
            )
        }

        composable(Routs.HOME) {
            HomeScreen(navController)

        }
        composable(
            route = "${Routs.DETAILS}/{url}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            ArticleDetailScreen(navController = navController, url = url)
        }

    }

}