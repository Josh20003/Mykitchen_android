package com.example.mykitchen.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import com.example.mykitchen.R
import com.example.mykitchen.data.User
import com.example.mykitchen.ui.components.StatisticItem

@Composable
fun ProfileScreen(
    user: User?,
    onLogoutClick: () -> Unit,
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var editUsername by remember(user) { mutableStateOf(user?.username ?: "") }
    var editEmail by remember(user) { mutableStateOf(user?.email ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Text(
                user?.username?.take(2)?.uppercase() ?: "JO",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFFEE7B00), shape = androidx.compose.foundation.shape.CircleShape)
                    .padding(8.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                stringResource(R.string.profile),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                stringResource(R.string.update_your_info),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Profile Avatar
            Text(
                user?.username?.take(2)?.uppercase() ?: "JO",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFFEE7B00), shape = androidx.compose.foundation.shape.CircleShape)
                    .padding(24.dp)
            )

            // Edit Button
            Button(
                onClick = { isEditing = !isEditing },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEE7B00)
                )
            ) {
                Text(
                    stringResource(R.string.edit_profile),
                    color = Color.White
                )
            }

            // Username Field
            OutlinedTextField(
                value = editUsername,
                onValueChange = { editUsername = it },
                label = { Text(stringResource(R.string.username)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                readOnly = !isEditing,
                singleLine = true
            )

            // Email Field
            OutlinedTextField(
                value = editEmail,
                onValueChange = { editEmail = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                readOnly = !isEditing,
                singleLine = true
            )

            // Statistics Section
            Text(
                stringResource(R.string.statistics),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                stringResource(R.string.your_recipe_collection),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Stats Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                StatisticItem(
                    icon = R.drawable.ic_launcher_foreground,
                    value = user?.totalRecipes.toString(),
                    label = stringResource(R.string.total_recipes),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                StatisticItem(
                    icon = R.drawable.ic_launcher_foreground,
                    value = user?.microwaveRecipes.toString(),
                    label = stringResource(R.string.microwave),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
                StatisticItem(
                    icon = R.drawable.ic_launcher_foreground,
                    value = user?.noCookRecipes.toString(),
                    label = stringResource(R.string.no_cook),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Text("Logout", color = Color.Black)
            }
        }
    }
}

