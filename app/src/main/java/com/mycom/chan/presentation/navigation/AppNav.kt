package com.mycom.chan.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mycom.chan.presentation.page.Feed
import com.mycom.chan.presentation.page.Login
import com.mycom.chan.presentation.page.Register

/**
 * Định nghĩa các routes trong app
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Feed : Screen("feed")
    object ForgotPassword : Screen("forgot_password")
}

/**
 * NavGraph chính của ứng dụng
 * Quản lý navigation giữa các màn hình
 */
@Composable
fun AppNav(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(route = Screen.Login.route) {
            Login(
                onLoginSuccess = {
                    // Chuyển đến Feed và xóa Login khỏi back stack
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        // Register Screen
        composable(route = Screen.Register.route) {
            Register(
                onRegisterSuccess = {
                    // Có thể chuyển thẳng đến Feed hoặc về Login
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Feed Screen (Home)
        composable(
            route = Screen.Feed.route
        ) {
            Feed()
        }

        // TODO: Implement Forgot Password screen
        composable(
            route = Screen.ForgotPassword.route,
        ) {
            // ForgotPassword()
        }
    }
}
