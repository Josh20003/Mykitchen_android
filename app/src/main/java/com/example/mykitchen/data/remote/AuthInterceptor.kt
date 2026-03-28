package com.example.mykitchen.data.remote

import com.example.mykitchen.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Get token from session manager
        val token = sessionManager.getToken()

        // If token exists, add Authorization header
        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method(originalRequest.method, originalRequest.body)
                .build()
        } else {
            originalRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method(originalRequest.method, originalRequest.body)
                .build()
        }

        return chain.proceed(request)
    }
}
