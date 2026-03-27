package com.example.mykitchen.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RestaurantMenu() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(text = "Restaurant Menu")
        Text(text = "Food 1")
        Text(text = "Food 2")
        Text(text = "Food 3")

    }

}