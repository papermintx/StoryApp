package com.example.storyapp.presentation.navigation

import android.app.Activity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.example.storyapp.presentation.authentication.login.LoginScreen
import com.example.storyapp.presentation.authentication.register.RegisterScreen
import com.example.storyapp.presentation.camera.CameraScreen
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
            route = NavScreen.Home.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            }
        ){
            HomeScreen(navController = navController)
        }
        composable(
            NavScreen.Login.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
            }
        ){
            LoginScreen(navController = navController)
        }
        composable(NavScreen.Register.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            }){
            RegisterScreen(navController = navController)
        }
        composable(
            NavScreen.AddStory.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            }
        ) {
            AddStoryScreen( navHostController = navController)
        }
        composable(
            NavScreen.Splash.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            }
            ){
            SplashScreen( navHostController = navController)
        }
        composable(
            NavScreen.DetailStory.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            }
        ) {backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                DetailStoryScreen(id = id, navHostController =  navController)
            }
        }
        composable(
            NavScreen.Camera.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            }
        ) {
            CameraScreen(activity, navController)
        }

    }
}