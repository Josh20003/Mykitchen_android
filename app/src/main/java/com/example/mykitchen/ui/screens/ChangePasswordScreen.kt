package com.example.mykitchen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mykitchen.ui.components.LoadingButton
import com.example.mykitchen.ui.components.OutlinedTextFieldWithError
import com.example.mykitchen.ui.viewmodel.ProfileViewModel
import com.example.mykitchen.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val changePasswordState by viewModel.changePasswordState.collectAsStateWithLifecycle()

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPasswordError by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // Handle success
    LaunchedEffect(changePasswordState) {
        if (changePasswordState is UiState.Success) {
            onNavigateBack()
        }
    }

    // Validation
    fun validate(): Boolean {
        var isValid = true

        if (currentPassword.isEmpty()) {
            currentPasswordError = "Current password is required"
            isValid = false
        } else {
            currentPasswordError = ""
        }

        if (newPassword.isEmpty()) {
            newPasswordError = "New password is required"
            isValid = false
        } else if (newPassword.length < 6) {
            newPasswordError = "Password must be at least 6 characters"
            isValid = false
        } else {
            newPasswordError = ""
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm your new password"
            isValid = false
        } else if (confirmPassword != newPassword) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        } else {
            confirmPasswordError = ""
        }

        return isValid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter your current password and a new password",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Current Password Field
            OutlinedTextFieldWithError(
                value = currentPassword,
                onValueChange = { 
                    currentPassword = it 
                    currentPasswordError = ""
                },
                label = "Current Password",
                isPassword = true,
                isError = currentPasswordError.isNotEmpty(),
                errorMessage = currentPasswordError,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Current Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Password Field
            OutlinedTextFieldWithError(
                value = newPassword,
                onValueChange = { 
                    newPassword = it 
                    newPasswordError = ""
                },
                label = "New Password",
                isPassword = true,
                isError = newPasswordError.isNotEmpty(),
                errorMessage = newPasswordError,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "New Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextFieldWithError(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it 
                    confirmPasswordError = ""
                },
                label = "Confirm New Password",
                isPassword = true,
                isError = confirmPasswordError.isNotEmpty(),
                errorMessage = confirmPasswordError,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )

            // Error message from API
            when (val state = changePasswordState) {
                is UiState.Error -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
                is UiState.Success -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Password changed successfully!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Change Password Button
            LoadingButton(
                onClick = {
                    if (validate()) {
                        viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                    }
                },
                isLoading = changePasswordState == UiState.Loading,
                text = "Change Password"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cancel Button
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}