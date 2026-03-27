package com.example.mykitchen.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
    object AddRecipe : Screen("add_recipe")
    object Profile : Screen("profile")
}

