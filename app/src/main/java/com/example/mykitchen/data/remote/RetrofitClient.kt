package com.example.mykitchen.data.remote

import android.content.Context
import com.example.mykitchen.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Base URL - Change this to your actual API URL
    // For local development use: "http://10.0.2.2:8000/" (Android emulator)
    // For production use your actual domain: "https://your-api-domain.com/"
    private const val BASE_URL = "https://your-api-url.com/"

    @Volatile
    private var retrofit: Retrofit? = null

    fun getInstance(context: Context): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: buildRetrofit(context).also { retrofit = it }
        }
    }

    private fun buildRetrofit(context: Context): Retrofit {
        val sessionManager = SessionManager(context)

        // Logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Auth interceptor to add Bearer token
        val authInterceptor = AuthInterceptor(sessionManager)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(context: Context): ApiService {
        return getInstance(context).create(ApiService::class.java)
    }

    // Call this when user logs out to reset the client
    fun reset() {
        retrofit = null
    }
}
