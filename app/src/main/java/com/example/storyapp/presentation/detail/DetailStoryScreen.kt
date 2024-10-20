package com.example.storyapp.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.storyapp.domain.ResultState
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.utils.fonts.MyTypography

@Composable
fun DetailStoryScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailStoryViewModel = hiltViewModel(),
    id: String,
    goBack: () -> Unit,
) {
    val storyDetail = viewModel.storyDetail.collectAsState()
    var imageLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getDetailStory(id)
    }
    if(!imageLoaded){
        LoadingDialog()
    }

    val scrollState = rememberScrollState()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 16.dp)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            )

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
                            onSuccess = { _, _ -> imageLoaded = true }
                        )
                        .build(),
                    contentDescription = story.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2),
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
                   goBack
                }, message = result.exception)
            }
            else -> Unit
        }
    }
}
