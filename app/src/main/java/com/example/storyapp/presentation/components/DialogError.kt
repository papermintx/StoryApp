package com.example.storyapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.storyapp.R
import com.example.storyapp.utils.fonts.MyTypography

@Composable
fun DialogError(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    message: String,
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },

            title = {
                Text("Error",
                    style = MyTypography.titleMedium,
                    )
                    },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    // Lottie animation for the error
                    LottieAnimation(
                        composition = composition,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        message,
                        style = MyTypography.titleMedium
                    )
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }

}
