package com.example.mykitchen.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykitchen.data.local.SessionManager
import com.example.mykitchen.data.remote.RetrofitClient
import com.example.mykitchen.data.remote.safeApiCall
import com.example.mykitchen.data.remote.model.DashboardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)
    private val apiService = RetrofitClient.getApiService(context)

    // Dashboard state
    private val _dashboardState = MutableStateFlow<UiState<DashboardResponse>>(UiState.Initial)
    val dashboardState: StateFlow<UiState<DashboardResponse>> = _dashboardState.asStateFlow()

    // Logout state
    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val logoutState: StateFlow<UiState<Unit>> = _logoutState.asStateFlow()

    // User name for display
    val userName: String?
        get() = sessionManager.getUserName()

    fun loadDashboard() {
        viewModelScope.launch {
            _dashboardState.value = UiState.Loading

            // MOCK MODE: For testing without backend - ENABLED
            kotlinx.coroutines.delay(800) // Simulate network delay
            val mockResponse = com.example.mykitchen.data.remote.model.DashboardResponse(
                success = true,
                message = "Welcome to MyKitchen!",
                data = com.example.mykitchen.data.remote.model.DashboardData(
                    user = com.example.mykitchen.data.remote.model.UserResponse(
                        id = 1,
                        name = sessionManager.getUserName() ?: "Test User",
                        email = sessionManager.getUserEmail() ?: "test@example.com"
                    ),
                    stats = com.example.mykitchen.data.remote.model.DashboardStats(
                        recipes = 12,
                        favorites = 5,
                        total_cook_time = 180
                    )
                )
            )
            _dashboardState.value = UiState.Success(mockResponse)
            return@launch

            val result = safeApiCall {
                apiService.getDashboard()
            }

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    val response = result.data
                    if (response.success) {
                        _dashboardState.value = UiState.Success(response)
                    } else {
                        _dashboardState.value = UiState.Error(response.message)
                    }
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    _dashboardState.value = UiState.Error(result.message)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    _dashboardState.value = UiState.Error(result.message)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading

            val result = safeApiCall {
                apiService.logout()
            }

            // Clear session regardless of API result
            sessionManager.clearSession()
            RetrofitClient.reset()

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    _logoutState.value = UiState.Success(Unit)
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    // Still consider logout successful locally
                    _logoutState.value = UiState.Success(Unit)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    // Still consider logout successful locally
                    _logoutState.value = UiState.Success(Unit)
                }
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = UiState.Initial
    }
}
