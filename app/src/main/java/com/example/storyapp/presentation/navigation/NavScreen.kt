package com.example.storyapp.presentation.navigation

sealed class NavScreen(val route: String) {
    data object Home: NavScreen("home")
    data object Login: NavScreen("login")
    data object Register: NavScreen("register")
    data object AddStory: NavScreen("add_story")
    data object Splash: NavScreen("splash")
    data object DetailStory: NavScreen("detail_story/{id}"){
        fun createRoute(id: String) = "detail_story/$id"
    }

    data object Camera: NavScreen("camera")
}