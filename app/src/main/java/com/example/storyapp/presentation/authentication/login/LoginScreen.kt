package com.example.storyapp.presentation.authentication.login

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.presentation.authentication.components.CustomTextField
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.navigation.NavScreen


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val loginState = viewModel.loginState.collectAsState()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current



    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    when (val data = loginState.value) {
        is ResultState.Error -> {
            DialogError(onDismiss = {
                         viewModel.resetState()
            }, message = data.exception)
        }
        ResultState.Idle -> Unit
        ResultState.Loading -> LoadingDialog()
        is ResultState.Success -> {
            navController.navigate(NavScreen.Home.route) {
                popUpTo(NavScreen.Login.route) { inclusive = true }
                Log.d("LoginScreen", "Success")
                viewModel.resetState()
            }
        }
    }

    // UI Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Input Email
        CustomTextField(
            label = "Email",
            text = email.value,
            onTextChange = { email.value = it },
            validate = {
                if (it.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(it).matches())
                    "Invalid email address."
                else ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Input Password
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
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
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
    }
}
