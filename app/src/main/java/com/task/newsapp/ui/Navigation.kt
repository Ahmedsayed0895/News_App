package com.task.newsapp.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.task.newsapp.ui.screens.articleDetails.ArticleDetailScreen
import com.task.newsapp.ui.screens.favoriteArticles.FavoriteArticleScreen
import com.task.newsapp.ui.screens.home.HomeScreen
import com.task.newsapp.ui.screens.login.LoginScreen
import com.task.newsapp.ui.screens.profile.ProfileScreen
import com.task.newsapp.ui.screens.register.RegisterScreen
import com.task.newsapp.ui.theme.PrimaryBlue
import com.task.newsapp.ui.theme.White
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val DETAILS = "details"
    const val PROFILE = "profile"
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavItem(Routes.HOME, "Home", Icons.Default.Home)
    data object Favorites : BottomNavItem(Routes.FAVORITES, "Favorites", Icons.Default.Favorite)
    data object Profile : BottomNavItem(Routes.PROFILE, "Profile", Icons.Default.Person)
}

@Composable
fun AppNavigation(
    startDestination: String = Routes.LOGIN
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarScreens = listOf(Routes.HOME, Routes.FAVORITES, Routes.PROFILE)
    val showBottomBar = bottomBarScreens.any { currentDestination?.route?.startsWith(it) == true }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                ) {
                    val items = listOf(BottomNavItem.Home, BottomNavItem.Favorites, BottomNavItem.Profile)

                    items.forEach { item ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(28.dp),
                                )
                            },
                            label = { Text(text = item.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = PrimaryBlue,
                                selectedIconColor = White,
                                selectedTextColor = PrimaryBlue,
                            ),

                        )
                    }
                }
            }
        }
    ) { _ ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }

        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterSuccess = { navController.navigate(Routes.LOGIN) },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(navController = navController)
            }

            composable(Routes.FAVORITES) {
                FavoriteArticleScreen(navController = navController)
            }

            composable(
                route = "${Routes.DETAILS}/{url}",
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url") ?: ""
                val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
                ArticleDetailScreen(navController = navController, url = decodedUrl)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(navController = navController)
            }
        }
    }
}