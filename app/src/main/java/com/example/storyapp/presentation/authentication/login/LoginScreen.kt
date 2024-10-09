package com.example.storyapp.presentation.authentication.login

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
import androidx.compose.runtime.DisposableEffect
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
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.presentation.authentication.components.CustomTextField
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.navigation.NavScreen
import com.example.storyapp.utils.fonts.MyTypography

@SuppressLint("RememberReturnType")
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

    val animationAlpha = remember { Animatable(0f) }
    val animationOffsetX = remember { Animatable(0f) }

    val emailAlpha = remember { Animatable(0f) }
    val passwordAlpha = remember { Animatable(0f) }
    val buttonAlpha = remember { Animatable(0f) }
    val buttonAlpha2 = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationAlpha.animateTo(1f, animationSpec = tween(durationMillis = 700))

        textAlpha.animateTo(1f, animationSpec = tween(durationMillis = 200, delayMillis = 40))

        emailAlpha.animateTo(1f, animationSpec = tween(durationMillis = 200,delayMillis = 40))

        passwordAlpha.animateTo(1f, animationSpec = tween(durationMillis = 200, delayMillis = 40))

        buttonAlpha.animateTo(1f, animationSpec = tween(durationMillis = 200, delayMillis = 40))

        buttonAlpha2.animateTo(1f, animationSpec = tween(durationMillis = 200, delayMillis = 40))

    }

    LaunchedEffect(Unit) {
        while (true) {
            animationOffsetX.animateTo(
                targetValue = 80f,
                animationSpec = tween(durationMillis = 9000)
            )
            animationOffsetX.animateTo(
                targetValue = -80f,
                animationSpec = tween(durationMillis = 9000)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    when (val data = loginState.value) {
        is ResultState.Error -> {
            DialogError(onDismiss = { viewModel.resetState() }, message = data.exception)
        }
        ResultState.Idle -> Unit
        ResultState.Loading -> LoadingDialog()
        is ResultState.Success -> {
            navController.navigate(NavScreen.Home.route) {
                popUpTo(NavScreen.Login.route) { inclusive = true }
                viewModel.resetState()
            }
        }
    }

    // UI Layout dengan animasi fade-in
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(25.dp)
           ,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.image_login), contentDescription = null, modifier = Modifier.graphicsLayer(
            alpha = animationAlpha.value,
            translationX = animationOffsetX.value
        ))
        Text(
            text = "Ready to share your story?",
            style = MyTypography.headlineSmall,
            modifier = Modifier.graphicsLayer(alpha = textAlpha.value)
        )
        Text(
            text = "Please enter your data first",
            style = MyTypography.bodyLarge,
            modifier = Modifier.graphicsLayer(alpha = textAlpha.value)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Email", style = MyTypography.bodyMedium, modifier = Modifier.graphicsLayer(alpha = emailAlpha.value)
        )
        CustomTextField(
            label = "Email",
            text = email.value,
            onTextChange = { email.value = it },
            validate = {
                if (it.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(it).matches())
                    "Invalid email address."
                else ""
            },
            modifier = Modifier.graphicsLayer(alpha = emailAlpha.value)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Input Password
        Text(text = "Password", style = MyTypography.bodyMedium, modifier = Modifier.graphicsLayer(alpha = passwordAlpha.value))
        CustomTextField(
            label = "Password",
            text = password.value,
            onTextChange = { password.value = it },
            isPassword = true,
            validate = {
                if (it.length < 8)
                    "Password must be at least 8 characters."
                else ""
            },
            modifier = Modifier.graphicsLayer(alpha = passwordAlpha.value)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.value.isNotBlank() && password.value.isNotBlank()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches() && password.value.length >= 8) {
                        viewModel.login(LoginRequest(email.value, password.value))
                    } else {
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.fillMaxWidth().height(50.dp) .graphicsLayer(alpha = buttonAlpha.value)
        ) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Register
        Button(
            onClick = {
                navController.navigate(NavScreen.Register.route)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp) .graphicsLayer(alpha = buttonAlpha2.value)
        ) {
            Text(text = "Register")
        }
    }
}
