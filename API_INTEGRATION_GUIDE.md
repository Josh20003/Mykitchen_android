# API Integration Guide - MyKitchen Mobile App

This document provides guidance for integrating backend services with the MyKitchen mobile application.

## Current Architecture

The app currently uses in-memory state management with `AppViewModel` and `StateFlow`. This guide explains how to integrate with a backend API.

## Recommended Approach: Retrofit + Room Database

### 1. Add Required Dependencies

Update `gradle/libs.versions.toml`:

```toml
[versions]
...
retrofit = "2.9.0"
okhttp = "4.11.0"
roomDatabase = "2.5.1"
moshi = "1.15.0"

[libraries]
...
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-moshi = { group = "com.squareup.retrofit2", name = "converter-moshi", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomDatabase" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomDatabase" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomDatabase" }
moshi = { group = "com.squareup.moshi", name = "moshi-kotlin", version.ref = "moshi" }
```

### 2. API Service Interface

Create `data/api/ApiService.kt`:

```kotlin
package com.example.mykitchen.data.api

import com.example.mykitchen.data.User
import com.example.mykitchen.data.Recipe
import retrofit2.http.*

interface ApiService {
    
    // Authentication
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<AuthResponse>
    
    @POST("auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): ApiResponse<AuthResponse>
    
    // User
    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): ApiResponse<User>
    
    @PUT("user/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body user: User
    ): ApiResponse<User>
    
    // Recipes
    @GET("recipes")
    suspend fun getRecipes(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<List<Recipe>>
    
    @POST("recipes")
    suspend fun createRecipe(
        @Header("Authorization") token: String,
        @Body recipe: Recipe
    ): ApiResponse<Recipe>
    
    @GET("recipes/{id}")
    suspend fun getRecipe(
        @Header("Authorization") token: String,
        @Path("id") recipeId: String
    ): ApiResponse<Recipe>
    
    @PUT("recipes/{id}")
    suspend fun updateRecipe(
        @Header("Authorization") token: String,
        @Path("id") recipeId: String,
        @Body recipe: Recipe
    ): ApiResponse<Recipe>
    
    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(
        @Header("Authorization") token: String,
        @Path("id") recipeId: String
    ): ApiResponse<Unit>
}

// Request/Response Models
data class LoginRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String
)

data class AuthResponse(
    val user: User,
    val token: String,
    val refreshToken: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String?
)
```

### 3. Repository Pattern

Create `data/repository/RecipeRepository.kt`:

```kotlin
package com.example.mykitchen.data.repository

import com.example.mykitchen.data.Recipe
import com.example.mykitchen.data.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RecipeRepository {
    suspend fun getRecipes(): Result<List<Recipe>>
    suspend fun addRecipe(recipe: Recipe): Result<Recipe>
    suspend fun deleteRecipe(recipeId: String): Result<Unit>
    suspend fun updateRecipe(recipe: Recipe): Result<Recipe>
}

class RecipeRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : RecipeRepository {
    
    override suspend fun getRecipes(): Result<List<Recipe>> = try {
        val token = tokenManager.getToken()
        val response = apiService.getRecipes(token = "Bearer $token")
        if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.error ?: "Unknown error"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    override suspend fun addRecipe(recipe: Recipe): Result<Recipe> = try {
        val token = tokenManager.getToken()
        val response = apiService.createRecipe(
            token = "Bearer $token",
            recipe = recipe
        )
        if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.error ?: "Failed to create recipe"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    override suspend fun deleteRecipe(recipeId: String): Result<Unit> = try {
        val token = tokenManager.getToken()
        val response = apiService.deleteRecipe(
            token = "Bearer $token",
            recipeId = recipeId
        )
        if (response.success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.error ?: "Failed to delete recipe"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    override suspend fun updateRecipe(recipe: Recipe): Result<Recipe> = try {
        val token = tokenManager.getToken()
        val response = apiService.updateRecipe(
            token = "Bearer $token",
            recipeId = recipe.id,
            recipe = recipe
        )
        if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.error ?: "Failed to update recipe"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 4. Token Management

Create `data/TokenManager.kt`:

```kotlin
package com.example.mykitchen.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(private val context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun saveTokens(accessToken: String, refreshToken: String) {
        encryptedSharedPreferences.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }
    
    fun getToken(): String {
        return encryptedSharedPreferences.getString("access_token", "") ?: ""
    }
    
    fun getRefreshToken(): String {
        return encryptedSharedPreferences.getString("refresh_token", "") ?: ""
    }
    
    fun clearTokens() {
        encryptedSharedPreferences.edit().clear().apply()
    }
    
    fun isTokenExpired(): Boolean {
        // Implement JWT token expiration check
        return false
    }
}
```

### 5. Update AppViewModel

Modify `ui/viewmodel/AppViewModel.kt` to use repository:

```kotlin
class AppViewModel(
    private val recipeRepository: RecipeRepository = /* inject */,
    private val authRepository: AuthRepository = /* inject */
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()
    
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = authRepository.login(email, password)
                result.onSuccess { authResponse ->
                    _currentUser.value = authResponse.user
                    _authState.value = AuthState.Authenticated(authResponse.user)
                    loadRecipes()
                }.onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }
    
    private fun loadRecipes() {
        viewModelScope.launch {
            try {
                val result = recipeRepository.getRecipes()
                result.onSuccess { recipes ->
                    _recipes.value = recipes
                }.onFailure { error ->
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    override fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val result = recipeRepository.addRecipe(recipe)
                result.onSuccess { newRecipe ->
                    val updatedRecipes = _recipes.value.toMutableList()
                    updatedRecipes.add(newRecipe)
                    _recipes.value = updatedRecipes
                }.onFailure { error ->
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
```

## Dependency Injection Setup

### Using Hilt (Recommended)

Add to `build.gradle.kts`:
```kotlin
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")
```

Create `di/AppModule.kt`:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    
    @Singleton
    @Provides
    fun provideRecipeRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ): RecipeRepository {
        return RecipeRepositoryImpl(apiService, tokenManager)
    }
}
```

## Error Handling

Implement proper error handling:

```kotlin
sealed class ApiError : Exception() {
    data class NetworkError(val message: String) : ApiError()
    data class ServerError(val code: Int, val message: String) : ApiError()
    data class UnauthorizedError : ApiError()
    data class NotFoundError : ApiError()
    data class ValidationError(val errors: Map<String, String>) : ApiError()
    data class UnknownError(val message: String) : ApiError()
}
```

## Testing Backend Integration

```kotlin
@Test
fun testLoginSuccess() = runTest {
    val mockApiService = mockk<ApiService>()
    coEvery { mockApiService.login(any()) } returns ApiResponse(
        success = true,
        data = AuthResponse(
            user = User(email = "test@example.com"),
            token = "fake_token",
            refreshToken = "fake_refresh"
        ),
        error = null
    )
    
    val viewModel = AppViewModel(
        authRepository = AuthRepositoryImpl(mockApiService)
    )
    
    viewModel.login("test@example.com", "password")
    
    advanceUntilIdle()
    
    assertIs<AuthState.Authenticated>(viewModel.authState.value)
}
```

## Security Considerations

1. **HTTPS Only**: Ensure all API calls use HTTPS
2. **Token Storage**: Use EncryptedSharedPreferences for token storage
3. **Certificate Pinning**: Implement for production
4. **Input Validation**: Validate all user inputs before sending
5. **Rate Limiting**: Implement exponential backoff for retries
6. **Token Refresh**: Automatically refresh expired tokens
7. **Logout**: Clear all stored credentials on logout

## Deployment Checklist

- [ ] Backend API endpoints tested and ready
- [ ] Authentication flow implemented
- [ ] Error handling for all endpoints
- [ ] Offline mode strategy defined
- [ ] Data validation rules documented
- [ ] Security review completed
- [ ] Rate limiting configured
- [ ] Token refresh mechanism tested
- [ ] Logging configured for debugging
- [ ] Analytics integration ready

## Useful Resources

- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Moshi JSON Parser](https://github.com/square/moshi)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://dagger.dev/hilt/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**Last Updated**: March 18, 2026
**Version**: 1.0.0

