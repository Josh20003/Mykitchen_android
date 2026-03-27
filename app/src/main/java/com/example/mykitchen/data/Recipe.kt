package com.example.mykitchen.data

data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val cookTime: Int = 0,
    val category: String = "Fried",
    val userId: String = ""
)

