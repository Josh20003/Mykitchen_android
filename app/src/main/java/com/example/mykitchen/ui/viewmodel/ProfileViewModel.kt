package com.example.mykitchen.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykitchen.data.local.SessionManager
import com.example.mykitchen.data.remote.RetrofitClient
import com.example.mykitchen.data.remote.safeApiCall
import com.example.mykitchen.data.remote.model.ChangePasswordRequest
import com.example.mykitchen.data.remote.model.ProfileResponse
import com.example.mykitchen.data.remote.model.UpdateProfileRequest
import com.example.mykitchen.data.remote.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)
    private val apiService = RetrofitClient.getApiService(context)

    // Profile state
    private val _profileState = MutableStateFlow<UiState<ProfileData>>(UiState.Initial)
    val profileState: StateFlow<UiState<ProfileData>> = _profileState.asStateFlow()

    // Update profile state
    private val _updateProfileState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val updateProfileState: StateFlow<UiState<Unit>> = _updateProfileState.asStateFlow()

    // Change password state
    private val _changePasswordState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val changePasswordState: StateFlow<UiState<Unit>> = _changePasswordState.asStateFlow()

    data class ProfileData(
        val user: UserResponse
    )

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = UiState.Loading

            // MOCK MODE: For testing without backend - ENABLED
            kotlinx.coroutines.delay(600) // Simulate network delay
            val mockUser = com.example.mykitchen.data.remote.model.UserResponse(
                id = 1,
                name = sessionManager.getUserName() ?: "Test User",
                email = sessionManager.getUserEmail() ?: "test@example.com"
            )
            _profileState.value = UiState.Success(ProfileData(mockUser))
            return@launch

            val result = safeApiCall {
                apiService.getProfile()
            }

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    val response = result.data
                    if (response.success) {
                        // Update local session with latest user data
                        sessionManager.saveUser(response.user)
                        _profileState.value = UiState.Success(ProfileData(response.user))
                    } else {
                        _profileState.value = UiState.Error(response.message)
                    }
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    _profileState.value = UiState.Error(result.message)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    _profileState.value = UiState.Error(result.message)
                }
            }
        }
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            _updateProfileState.value = UiState.Loading

            val result = safeApiCall {
                apiService.updateProfile(UpdateProfileRequest(name, email))
            }

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    val response = result.data
                    if (response.success) {
                        // Update local session with new user data
                        sessionManager.saveUser(response.user)
                        _updateProfileState.value = UiState.Success(Unit)
                        // Refresh profile data
                        _profileState.value = UiState.Success(ProfileData(response.user))
                    } else {
                        _updateProfileState.value = UiState.Error(response.message)
                    }
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    _updateProfileState.value = UiState.Error(result.message)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    _updateProfileState.value = UiState.Error(result.message)
                }
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = UiState.Loading

            val result = safeApiCall {
                apiService.changePassword(
                    ChangePasswordRequest(
                        current_password = currentPassword,
                        new_password = newPassword,
                        new_password_confirmation = confirmPassword
                    )
                )
            }

            when (result) {
                is com.example.mykitchen.data.remote.ApiResult.Success -> {
                    val response = result.data
                    if (response.success) {
                        _changePasswordState.value = UiState.Success(Unit)
                    } else {
                        _changePasswordState.value = UiState.Error(response.message)
                    }
                }
                is com.example.mykitchen.data.remote.ApiResult.Error -> {
                    _changePasswordState.value = UiState.Error(result.message)
                }
                is com.example.mykitchen.data.remote.ApiResult.NetworkError -> {
                    _changePasswordState.value = UiState.Error(result.message)
                }
            }
        }
    }

    fun resetUpdateProfileState() {
        _updateProfileState.value = UiState.Initial
    }

    fun resetChangePasswordState() {
        _changePasswordState.value = UiState.Initial
    }
}
