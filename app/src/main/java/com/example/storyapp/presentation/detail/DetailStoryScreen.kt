package com.example.storyapp.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.storyapp.domain.ResultState
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.navigation.NavScreen
import com.example.storyapp.utils.fonts.MyTypography

@Composable
fun DetailStoryScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailStoryViewModel = hiltViewModel(),
    id: String,
    navHostController: NavHostController
) {
    val storyDetail = viewModel.storyDetail.collectAsState()
    var imageLoaded by remember { mutableStateOf(false) } // State untuk mengecek apakah gambar sudah dimuat

    LaunchedEffect(Unit) {
        viewModel.getDetailStory(id)
    }
    if(!imageLoaded){
        LoadingDialog()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 16.dp),
    ) {

        when (val result = storyDetail.value) {
            is ResultState.Success -> {
                val story = result.data

                // Image dengan animasi saat dimuat
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(story.photoUrl)
                        .crossfade(true) // Efek crossfade
                        .listener(
                            onSuccess = { _, _ -> imageLoaded = true } // Set imageLoaded ke true saat gambar berhasil dimuat
                        )
                        .build(),
                    contentDescription = story.description,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                AnimatedVisibility(visible = imageLoaded) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = story.name,
                            style = MyTypography.titleLarge,
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = story.description,
                            style = MyTypography.bodyMedium,
                        )
                    }
                }
            }
            is ResultState.Error -> {
                DialogError(onDismiss = {
                    navHostController.popBackStack()
                }, message = result.exception)
            }
            else -> Unit
        }
    }
}
