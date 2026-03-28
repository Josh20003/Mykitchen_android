package com.example.mykitchen.data.remote

import com.example.mykitchen.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Authentication Endpoints
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/logout")
    suspend fun logout(): Response<LogoutResponse>

    // Profile Endpoints
    @GET("api/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @PUT("api/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    @PUT("api/profile/password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    // Dashboard Endpoint
    @GET("api/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>
}
