package com.example.mykitchen.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mykitchen.data.local.SessionManager
import com.example.mykitchen.ui.screens.*
import com.example.mykitchen.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Profile : Screen("profile")
    object UpdateProfile : Screen("update_profile")
    object ChangePassword : Screen("change_password")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Check if user is logged in
    val startDestination = if (sessionManager.isLoggedIn()) {
        Screen.Dashboard.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            val viewModel: AuthViewModel = viewModel(factory = ViewModelFactory(context))
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                viewModel = viewModel
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            val viewModel: AuthViewModel = viewModel(factory = ViewModelFactory(context))
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        // Dashboard Screen
        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = viewModel(factory = ViewModelFactory(context))
            DashboardScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        // Profile Screen
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToUpdateProfile = {
                    navController.navigate(Screen.UpdateProfile.route)
                },
                onNavigateToChangePassword = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                viewModel = viewModel
            )
        }

        // Update Profile Screen
        composable(Screen.UpdateProfile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
            UpdateProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        // Change Password Screen
        composable(Screen.ChangePassword.route) {
            val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
            ChangePasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}