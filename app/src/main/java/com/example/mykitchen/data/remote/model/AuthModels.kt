package com.example.mykitchen.data.remote.model

// Request Models
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)

data class ChangePasswordRequest(
    val current_password: String,
    val new_password: String,
    val new_password_confirmation: String
)

data class UpdateProfileRequest(
    val name: String,
    val email: String
)

// Response Models
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserResponse? = null
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserResponse? = null
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val created_at: String? = null,
    val updated_at: String? = null
)

data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val user: UserResponse
)

data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val user: UserResponse
)

data class ChangePasswordResponse(
    val success: Boolean,
    val message: String
)

data class LogoutResponse(
    val success: Boolean,
    val message: String
)

// Dashboard Response Models
data class DashboardResponse(
    val success: Boolean,
    val message: String,
    val data: DashboardData? = null
)

data class DashboardData(
    val user: UserResponse,
    val stats: DashboardStats? = null
)

data class DashboardStats(
    val recipes: Int = 0,
    val favorites: Int = 0,
    val total_cook_time: Int = 0
)

// Error Response
data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val errors: Map<String, List<String>>? = null
)
