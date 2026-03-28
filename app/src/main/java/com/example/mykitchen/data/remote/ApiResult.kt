package com.example.mykitchen.data.remote

// Sealed class to represent API call results
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String) : ApiResult<Nothing>()
    data class NetworkError(val message: String) : ApiResult<Nothing>()
}

// Extension function to handle API responses
suspend fun <T> safeApiCall(call: suspend () -> retrofit2.Response<T>): ApiResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(response.code(), "Response body is null")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val message = when (response.code()) {
                400 -> "Bad request. Please check your input."
                401 -> "Unauthorized. Please login again."
                403 -> "Access denied."
                404 -> "Resource not found."
                422 -> "Validation failed. Please check your input."
                500 -> "Server error. Please try again later."
                else -> errorBody ?: "An error occurred"
            }
            ApiResult.Error(response.code(), message)
        }
    } catch (e: java.net.UnknownHostException) {
        ApiResult.NetworkError("No internet connection. Please check your network.")
    } catch (e: java.net.SocketTimeoutException) {
        ApiResult.NetworkError("Connection timed out. Please try again.")
    } catch (e: Exception) {
        ApiResult.NetworkError(e.message ?: "An unexpected error occurred")
    }
}
