package com.example.storyapp.presentation.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.storyapp.R
import com.example.storyapp.presentation.navigation.NavScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val composition2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading_splash)
    )
    val context = LocalContext.current
    val token by viewModel.tokenNotNull.collectAsState()
    LaunchedEffect(Unit) {
        delay(3000)
        if (token?.isNotEmpty() == true) {
            navHostController.navigate(NavScreen.Home.route) {
                popUpTo(NavScreen.Splash.route) { inclusive = true }
                Toast.makeText(context, "Welcome Back", Toast.LENGTH_SHORT).show()
            }
        } else {
            navHostController.navigate(NavScreen.Login.route) {
                popUpTo(NavScreen.Splash.route) { inclusive = true }

            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.image_dicoding), contentDescription = null)
        Spacer(modifier = Modifier.height(16.dp))
        LottieAnimation(
            composition = composition2,
            modifier = Modifier.size(200.dp),
            iterations = Int.MAX_VALUE
        )
    }
}
