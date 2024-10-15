package com.example.storyapp.presentation.authentication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.storyapp.R

@Composable
fun CustomTextField(
    label: String,
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    isPassword: Boolean = false,
    validate: (String) -> String = { "" }
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            prefix = {
                if (isPassword) {
                    Icon(painter = painterResource(R.drawable.baseline_lock_24), contentDescription = null)
                }
                if (label == "Email") {
                    Icon(painter = painterResource(R.drawable.baseline_email_24), contentDescription = null)
                }
                if (label == "Name") {
                    Icon(painter = painterResource(R.drawable.baseline_person_24), contentDescription = null)
                }
            },
            onValueChange = { newValue ->
                onTextChange(newValue)
                errorMessage = validate(newValue)
                isError = errorMessage.isNotEmpty()
            },
            isError = isError,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                        )
                    }
                }
            },

            // belum di tes
            keyboardActions = KeyboardActions(
                onDone = {
                    errorMessage = validate(text)
                    isError = errorMessage.isNotEmpty()
                }
            ),

            // belum di tes
            keyboardOptions = KeyboardOptions.Default,
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
