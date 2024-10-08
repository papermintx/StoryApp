package com.example.storyapp.presentation.navigation

import android.app.Activity
import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.storyapp.presentation.add.story.AddStoryScreen
import com.example.storyapp.presentation.camera.CameraScreen
import com.example.storyapp.presentation.authentication.login.LoginScreen
import com.example.storyapp.presentation.authentication.register.RegisterScreen
import com.example.storyapp.presentation.detail.DetailStoryScreen
import com.example.storyapp.presentation.home.HomeScreen
import com.example.storyapp.presentation.splash.SplashScreen

@Composable
fun Navigation(modifier: Modifier = Modifier, viewModel: NavigationViewModel = hiltViewModel(), activity: Activity
) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        viewModel.navController = navController
    }

    NavHost(
        navController = navController,
        startDestination = NavScreen.Splash.route,
    ){
        composable(
            NavScreen.Home.route,

            ){
            HomeScreen(navController = navController)
        }
        composable(NavScreen.Login.route){
            LoginScreen(navController = navController)
        }
        composable(NavScreen.Register.route){
            RegisterScreen(navController = navController)
        }
        composable(NavScreen.AddStory.route) {
            AddStoryScreen( navHostController = navController)
        }
        composable(NavScreen.Splash.route){
            SplashScreen( navHostController = navController)
        }
        composable(
            NavScreen.DetailStory.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                DetailStoryScreen(id = id, navHostController =  navController)
            }
        }
        composable(NavScreen.Camera.route) {
            CameraScreen(activity, navController)
        }

    }
}