package com.example.storyapp.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.storyapp.R
import com.example.storyapp.presentation.home.components.DicodingStory
import com.example.storyapp.presentation.navigation.NavScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val stories = viewModel.storyPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavScreen.MapsStories.route)
                    }) {
                        Icon(painter = painterResource(R.drawable.baseline_map_24), contentDescription = null)
                    }
                    IconButton(onClick = {
                        navController.navigate(NavScreen.Login.route) {
                            popUpTo(NavScreen.Home.route) { inclusive = true }
                        }
//                        viewModel.resetState()
//                        viewModel.logout()
                    }) {
                        Icon(painter = painterResource(R.drawable.baseline_logout_24), contentDescription = null)
                    }
                },
                title = { Text(text = "Dicoding Story") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavScreen.AddStory.route)
                },
                containerColor = Color.Blue,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Story")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        DicodingStory(
            modifier = Modifier.padding(paddingValues),
            stories = stories
        )
    }
}
