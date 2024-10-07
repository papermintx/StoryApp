package com.example.storyapp.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class NavigationViewModel : ViewModel() {
    var navController: NavHostController? = null

    fun navigate(route: String) {
        navController?.navigate(route)
    }

    fun navigateBack() {
        navController?.navigateUp()
    }
}