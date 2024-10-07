package com.example.storyapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storyapp.presentation.add.story.AddStoryScreen
import com.example.storyapp.presentation.authentication.LoginScreen
import com.example.storyapp.presentation.authentication.RegisterScreen
import com.example.storyapp.presentation.home.HomeScreen
import com.example.storyapp.presentation.splash.SplashScreen

@Composable
fun Navigation(modifier: Modifier = Modifier, viewModel: NavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        viewModel.navController = navController
    }

    NavHost(
        navController = navController,
        startDestination = NavScreen.Splash.route,
    ){
        composable(NavScreen.Home.route){
            HomeScreen(navController = navController)
        }
        composable(NavScreen.Login.route){
            LoginScreen(navController = navController)
        }
        composable(NavScreen.Register.route){
            RegisterScreen()
        }
        composable(NavScreen.AddStory.route) {
            AddStoryScreen()
        }
        composable(
            NavScreen.Splash.route
        ){
            SplashScreen( navHostController = navController)
        }

    }
}