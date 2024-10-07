package com.example.storyapp.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.RegisterRequest

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, viewModel: AuthenticationViewModel = hiltViewModel()) {
    val uiState = viewModel.registerState.collectAsState()

    var name = remember {
        mutableStateOf("")
    }

    var email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    // Ui Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Register Text
        Text(text = "Register", style = MaterialTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Name TextField
        TextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        TextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        TextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                viewModel.register(RegisterRequest(name = name.value, email = email.value, password = password.value))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Indicator
        if (uiState.value is ResultState.Loading) {
            CircularProgressIndicator()
        }

        // Error Text
        if (uiState.value is ResultState.Error) {
            Text(text = (uiState.value as ResultState.Error).exception, color = Color.Red)
        }
    }

}