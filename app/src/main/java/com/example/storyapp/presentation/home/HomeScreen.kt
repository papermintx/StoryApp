package com.example.storyapp.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.storyapp.R
import com.example.storyapp.presentation.home.components.DicodingStory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    goDetailScreen: (String) -> Unit,
    goMapsScreen: () -> Unit,
    goLoginScreen: () -> Unit,
    goAddStoryScreen: () -> Unit,
) {
    val stories = viewModel.storyPagingFlow.collectAsLazyPagingItems()
    val showDialog = remember {
        mutableStateOf(false)
    }

    if (showDialog.value){
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    goLoginScreen()
                    viewModel.resetState()
                    viewModel.logout()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = goMapsScreen) {
                        Icon(painter = painterResource(R.drawable.baseline_map_24), contentDescription = null)
                    }
                    IconButton(onClick = {
                        showDialog.value = true
                    }) {
                        Icon(painter = painterResource(R.drawable.baseline_logout_24), contentDescription = null)
                    }
                },
                title = { Text(text = "Dicoding Story") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = goAddStoryScreen,
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
            stories = stories,
            gotoDetail = goDetailScreen
        )
    }
}
