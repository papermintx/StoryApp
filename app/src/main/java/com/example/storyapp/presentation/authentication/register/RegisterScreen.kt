package com.example.storyapp.presentation.authentication.register

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.storyapp.R
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.presentation.authentication.components.CustomTextField
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.utils.fonts.MyTypography

@SuppressLint("RememberReturnType")
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
    val context = LocalContext.current

    // Animasi fade-in untuk seluruh kolom UI
    val animationVisible = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationVisible.animateTo(1f, animationSpec = tween(durationMillis = 1090))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(25.dp)
            .graphicsLayer(alpha = animationVisible.value), // Animasi fade-in untuk kolom
        verticalArrangement = Arrangement.Center
    ) {

        Image(painter = painterResource(R.drawable.image_signup), contentDescription = null)
        Text(text = "Please complete your personal data below", style = MyTypography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name", style = MyTypography.bodyMedium)
        CustomTextField(
            label = "Name",
            text = name.value,
            onTextChange = { name.value = it },
            validate = {
                if (it.isEmpty()) "Name cannot be empty."
                else ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Email", style = MyTypography.bodyMedium)
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

        Text(text = "Password", style = MyTypography.bodyMedium)
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

        // Tombol Register dengan animasi lebar
        Button(
            onClick = {
                if (name.value.isNotBlank() && email.value.isNotBlank() && password.value.isNotBlank()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches() && password.value.length >= 8) {
                        viewModel.register(RegisterRequest(name.value, email.value, password.value))
                    } else {
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
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
