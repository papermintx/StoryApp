package com.example.storyapp.presentation.navigation

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
import com.example.storyapp.presentation.maps.MapStories
import com.example.storyapp.presentation.splash.SplashScreen

@Composable
fun Navigation(activity: Activity) {

    val isRefresh = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val navController = rememberNavController()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    NavHost(
        navController = navController,
        startDestination = NavScreen.Splash.route,
    ){
        composable(
            route = NavScreen.Home.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ){
            HomeScreen(
                isRefresh = isRefresh.value,
                goDetailScreen = { id ->
                    navController.navigate(NavScreen.DetailStory.createRoute(id))
                },
                goMapsScreen = {
                    navController.navigate(NavScreen.MapsStories.route)
                },
                goLoginScreen = {
                    navController.navigate(NavScreen.Login.route){
                        popUpTo(NavScreen.Home.route){
                            inclusive = true
                        }
                    }
                },
                goAddStoryScreen = {
                    navController.navigate(NavScreen.AddStory.route)
                },
                currentBackStack = {
                    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(NavArgument.REFRESH)?.observeForever { refresh ->
                        isRefresh.value = refresh
                    }
                },
                resetRefresh = {
                    isRefresh.value = false
                }
            )
        }
        composable(
            NavScreen.Login.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ){
            LoginScreen(
                goHomeScreen = {
                    navController.navigate(NavScreen.Home.route){
                        popUpTo(NavScreen.Login.route){
                            inclusive = true
                        }
                    }
                },
                goRegisterScreen = {
                    navController.navigate(NavScreen.Register.route)
                }
            )
        }
        composable(NavScreen.Register.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }){
            RegisterScreen(
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(NavScreen.AddStory.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 },animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 },animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ) {
            AddStoryScreen(
                imageUri = selectedImageUri,
                currentBackStack = {
                    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Uri?>(NavArgument.PHOTOURI)?.observeForever { uri ->
                        selectedImageUri = uri
                    }
                },
                goCamera = {
                    navController.navigate(NavScreen.Camera.route)
                },
                goGallery = {
                    launcher.launch("image/*")
                },
                goBack = {
                    navController.popBackStack()
                },
                backHandler = {
                    selectedImageUri = null
                },
                refreshBackStack = { refresh ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(NavArgument.REFRESH, refresh)
                }
            )
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
            SplashScreen(
                goLoginScreen = {
                    navController.navigate(NavScreen.Login.route){
                        popUpTo(NavScreen.Splash.route){
                            inclusive = true
                        }
                    }
                },
                goHomeScreen = {
                    navController.navigate(NavScreen.Home.route){
                        popUpTo(NavScreen.Splash.route){
                            inclusive = true
                        }
                    }
                    Toast.makeText(context, "Welcome Back to StoryApp", Toast.LENGTH_SHORT).show()
                }
            )
        }
        composable(
            NavScreen.DetailStory.route,
            arguments = listOf(navArgument(NavArgument.STORYID) { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 },animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ) {backStackEntry ->
            val id = backStackEntry.arguments?.getString(NavArgument.STORYID)
            if (id != null) {
                DetailStoryScreen(id = id, goBack = {
                    navController.popBackStack()
                })
            }
        }
        composable(
            NavScreen.Camera.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ) {
            CameraScreen(
                currentBackstackEntry = { uri ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(NavArgument.PHOTOURI, uri)

                },
                goBack = {
                    navController.popBackStack()
                },
                activity = activity
            )
        }
        composable(
            NavScreen.MapsStories.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ) {
            MapStories()
        }

    }
}