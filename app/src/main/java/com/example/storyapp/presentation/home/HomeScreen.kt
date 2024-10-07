package com.example.storyapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.storyapp.domain.ResultState
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.home.components.DicodingStory
import com.example.storyapp.presentation.navigation.NavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {

    val listStory = viewModel.listStory.collectAsState()

    // Home Screen
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dicoding Story")
                },
                modifier = modifier
                    .height(75.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavScreen.AddStory.route)
                },
                containerColor = Color.Blue, // Warna FAB
                contentColor = Color.White // Warna isi FAB
            ) {
                // Icon untuk FAB
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End, // Posisi FAB
    ) {paddingValues ->

        when(val list = listStory.value){
            is ResultState.Success -> {
                Column(
                    modifier = modifier.padding(paddingValues = paddingValues)
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    DicodingStory(listStory = list.data)

                }
            }
            is ResultState.Error -> {
                DialogError(onDismiss = {

                }, message = list.exception)
            }
            is ResultState.Loading -> {
                LoadingDialog()
            }
            else -> {
                Text(text = "Something went wrong")
            }
        }


    }

}