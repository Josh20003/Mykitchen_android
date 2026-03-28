package com.example.mykitchen.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykitchen.data.local.SessionManager
import com.example.mykitchen.data.remote.RetrofitClient
import com.example.mykitchen.data.remote.safeApiCall
import com.example.mykitchen.data.remote.model.LoginRequest
import com.example.mykitchen.data.remote.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)
    private val apiService = RetrofitClient.getApiService(context)

    // Login state
    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    // Register state
    private val _registerState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val registerState: StateFlow<UiState<Unit>> = _registerState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading

            // MOCK MODE: For testing without backend - ENABLED
            kotlinx.coroutines.delay(1000) // Simulate network delay
            sessionManager.saveToken("mock_token_12345")
            sessionManager.saveUser(
                com.example.mykitchen.data.remote.model.UserResponse(
                    id = 1,
                    name = "Test User",
                    email = email
                )
            )
            _loginState.value = UiState.Success(Unit)
            return@launch

            val result = safeApiCall {
                apiService.login(LoginRequest(email, password))
            }

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    val response = result.data
                    if (response.success && response.token != null) {
                        // Save token and user data
                        sessionManager.saveToken(response.token)
                        response.user?.let { user ->
                            sessionManager.saveUser(user)
                        }
                        _loginState.value = UiState.Success(Unit)
                    } else {
                        _loginState.value = UiState.Error(response.message)
                    }
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    _loginState.value = UiState.Error(result.message)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    _loginState.value = UiState.Error(result.message)
                }
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registerState.value = UiState.Loading

            val result = safeApiCall {
                apiService.register(
                    RegisterRequest(
                        name = name,
                        email = email,
                        password = password,
                        password_confirmation = confirmPassword
                    )
                )
            }

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    val response = result.data
                    if (response.success && response.token != null) {
                        // Save token and user data
                        sessionManager.saveToken(response.token)
                        response.user?.let { user ->
                            sessionManager.saveUser(user)
                        }
                        _registerState.value = UiState.Success(Unit)
                    } else {
                        _registerState.value = UiState.Error(response.message)
                    }
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    _registerState.value = UiState.Error(result.message)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    _registerState.value = UiState.Error(result.message)
                }
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = UiState.Initial
    }

    fun resetRegisterState() {
        _registerState.value = UiState.Initial
    }
}
