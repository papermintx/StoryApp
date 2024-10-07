package com.example.storyapp.presentation.authentication

import android.widget.Toast
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.presentation.components.CustomTextField
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.navigation.NavScreen
import com.example.storyapp.presentation.navigation.NavigationViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, viewModel: AuthenticationViewModel = hiltViewModel(), navController: NavHostController) {
    val loginState = viewModel.loginState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }
    var email = remember {mutableStateOf("")}
    val password = remember {mutableStateOf("")}

    // UI Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CustomTextField(
            label = "Email",
            text = email.value ,
            onTextChange = {email.value = it},
            validate = {
                if (it.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches())
                    "Invalid email address."
                else ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password
        CustomTextField(
            label = "Password",
            text = password.value,
            onTextChange = { password.value = it },
            isPassword = true,
            validate = {
                if (it.length < 8)
                    "Password must be at least 8 characters."
                else ""
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Tombol Login
        Button(
            onClick = {
                if (email.value.isNotBlank() && password.value.isNotBlank()) {
                    viewModel.login(LoginRequest(email.value, password.value))
                } else {

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Register
        Button(
            onClick = {
                navController.navigate(NavScreen.Register.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }

        // Menampilkan status loading
        when (val data = loginState.value) {
            is ResultState.Loading -> {
                LoadingDialog()
            }
            is ResultState.Error -> {
                DialogError(onDismiss = { viewModel.resetState() }, message = data.exception)
            }
            is ResultState.Success -> {
                if (data.data) {
                    Toast.makeText(
                        LocalContext.current,
                        "Login Success",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        LocalContext.current,
                        "Login Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> Unit
        }

    }
}