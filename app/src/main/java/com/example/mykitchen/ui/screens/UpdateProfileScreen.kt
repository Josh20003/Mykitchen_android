package com.example.mykitchen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
fun UpdateProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateProfileState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    // Load initial data from profile
    LaunchedEffect(profileState) {
        when (val state = profileState) {
            is UiState.Success -> {
                name = state.data.user.name
                email = state.data.user.email
            }
            else -> {}
        }
    }

    // Handle update success
    LaunchedEffect(updateState) {
        if (updateState is UiState.Success) {
            onNavigateBack()
        }
    }

    // Validation
    fun validate(): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else if (name.length < 2) {
            nameError = "Name must be at least 2 characters"
            isValid = false
        } else {
            nameError = ""
        }

        if (email.isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = ""
        }

        return isValid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile") },
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
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Update your profile information",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name Field
            OutlinedTextFieldWithError(
                value = name,
                onValueChange = {
                    name = it
                    nameError = ""
                },
                label = "Full Name",
                isError = nameError.isNotEmpty(),
                errorMessage = nameError,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextFieldWithError(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = "Email",
                isError = emailError.isNotEmpty(),
                errorMessage = emailError,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // Error message from API
            when (val state = updateState) {
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
                        text = "Profile updated successfully!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Update Button
            LoadingButton(
                onClick = {
                    if (validate()) {
                        viewModel.updateProfile(name, email)
                    }
                },
                isLoading = updateState == UiState.Loading,
                text = "Update Profile"
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