package com.example.mykitchen.data

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val totalRecipes: Int = 0,
    val microwaveRecipes: Int = 0,
    val noCookRecipes: Int = 0
)

