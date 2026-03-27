package com.example.mykitchen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mykitchen.data.Recipe
import com.example.mykitchen.ui.screens.AddRecipeScreen
import com.example.mykitchen.ui.screens.DashboardScreen
import com.example.mykitchen.ui.screens.LoginScreen
import com.example.mykitchen.ui.screens.ProfileScreen
import com.example.mykitchen.ui.screens.SignUpScreen
import com.example.mykitchen.ui.viewmodel.AppViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel()
) {
    val authState by appViewModel.authState.collectAsState()
    val currentUser by appViewModel.currentUser.collectAsState()
    val recipes by appViewModel.recipes.collectAsState()

    val startDestination = if (authState is com.example.mykitchen.ui.viewmodel.AuthState.Authenticated) {
        Screen.Dashboard.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    appViewModel.login(email, password)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { email, username, password ->
                    appViewModel.signUp(email, username, password)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                user = currentUser,
                recipes = recipes,
                onAddRecipeClick = {
                    navController.navigate(Screen.AddRecipe.route)
                },
                onLogoutClick = {
                    appViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.AddRecipe.route) {
            AddRecipeScreen(
                onSaveRecipe = { recipe ->
                    appViewModel.addRecipe(recipe)
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                user = currentUser,
                onLogoutClick = {
                    appViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onUpdateClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

