package com.example.mykitchen.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.mykitchen.data.remote.model.UserResponse

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MyKitchenPrefs"
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Save auth token
    fun saveToken(token: String) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // Get auth token
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // Save user data
    fun saveUser(user: UserResponse) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            apply()
        }
    }

    // Get saved user
    fun getUser(): UserResponse? {
        val id = prefs.getInt(KEY_USER_ID, -1)
        val name = prefs.getString(KEY_USER_NAME, null)
        val email = prefs.getString(KEY_USER_EMAIL, null)

        return if (id != -1 && name != null && email != null) {
            UserResponse(id = id, name = name, email = email)
        } else {
            null
        }
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getToken() != null
    }

    // Clear all session data (logout)
    fun clearSession() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }

    // Get user name
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    // Get user email
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
}
