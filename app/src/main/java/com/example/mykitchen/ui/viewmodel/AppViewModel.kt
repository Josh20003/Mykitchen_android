
package com.example.mykitchen.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykitchen.data.Recipe
import com.example.mykitchen.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AppViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                // Simulate login
                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    email = email,
                    username = email.substringBefore("@"),
                    totalRecipes = 0,
                    microwaveRecipes = 0,
                    noCookRecipes = 0
                )
                _currentUser.value = user
                _authState.value = AuthState.Authenticated(user)
                // Initialize with empty recipes
                _recipes.value = emptyList()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, username: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                // Simulate sign up
                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    email = email,
                    username = username,
                    totalRecipes = 0,
                    microwaveRecipes = 0,
                    noCookRecipes = 0
                )
                _currentUser.value = user
                _authState.value = AuthState.Authenticated(user)
                _recipes.value = emptyList()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
        _currentUser.value = null
        _recipes.value = emptyList()
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val updatedRecipes = _recipes.value.toMutableList()
            updatedRecipes.add(recipe.copy(id = "recipe_${System.currentTimeMillis()}"))
            _recipes.value = updatedRecipes

            // Update user statistics
            val user = _currentUser.value ?: return@launch
            val updatedUser = user.copy(
                totalRecipes = updatedRecipes.size,
                microwaveRecipes = updatedRecipes.count { it.category == "Microwave" },
                noCookRecipes = updatedRecipes.count { it.category == "No-Cook" }
            )
            _currentUser.value = updatedUser
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            val updatedRecipes = _recipes.value.filterNot { it.id == recipeId }
            _recipes.value = updatedRecipes

            // Update user statistics
            val user = _currentUser.value ?: return@launch
            val updatedUser = user.copy(
                totalRecipes = updatedRecipes.size,
                microwaveRecipes = updatedRecipes.count { it.category == "Microwave" },
                noCookRecipes = updatedRecipes.count { it.category == "No-Cook" }
            )
            _currentUser.value = updatedUser
        }
    }
}

