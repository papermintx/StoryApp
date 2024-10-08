package com.example.storyapp.presentation.authentication.register

import android.util.Patterns
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.presentation.authentication.components.CustomTextField
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState = viewModel.registerState.collectAsState()

    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    var isError by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(
            label = "Name",
            text = name.value,
            onTextChange = { name.value = it },
            validate = {
                if (it.isEmpty()) "Name cannot be empty."
                else ""
            }
        )

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

        CustomTextField(
            label = "Password",
            text = password.value,
            onTextChange = { password.value = it },
            isPassword = true,
            validate = {
                if (it.length < 8) "Password must be at least 8 characters."
                else ""
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                if (name.value.isNotBlank() && email.value.isNotBlank() && password.value.isNotBlank()) {
                    viewModel.register(

                        RegisterRequest(
                            name = name.value,
                            email = email.value,
                            password = password.value
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }

        Spacer(modifier = Modifier.height(16.dp))


    }

    when (val result = uiState.value) {
        is ResultState.Loading -> {
            LoadingDialog()
        }

        is ResultState.Error -> {
           DialogError(onDismiss = {
                viewModel.resetState()
                                   }, message = result.exception)
        }
        is ResultState.Success -> {
            viewModel.resetState()
            navController.popBackStack()
        }
        else -> Unit
    }
}
