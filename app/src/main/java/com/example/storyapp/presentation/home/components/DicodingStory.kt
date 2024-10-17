package com.example.storyapp.presentation.home.components

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.storyapp.domain.model.Story
import com.example.storyapp.presentation.components.LoadingDialog
import kotlinx.coroutines.delay

@Composable
fun DicodingStory(modifier: Modifier = Modifier, stories: LazyPagingItems<Story>) {

    val context = LocalContext.current
    val listState = rememberLazyListState()

    val firstItemVisible = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(stories.loadState) {
        if (stories.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (stories.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        if (stories.loadState.refresh is LoadState.Error) {
            LoadingDialog()
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize().padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(stories.itemCount) { index ->
                    val data = stories[index] ?: return@items

//                    val alpha = remember {
//                        Animatable(0f)
//                    }
//
//                    val opsetY = remember {
//                        Animatable(20f)
//                    }

//                    LaunchedEffect(firstItemVisible) {
//                        if (listState.firstVisibleItemIndex <= index) {
//                            delay((index - listState.firstVisibleItemIndex) * 90L)
//                            alpha.animateTo(1f, animationSpec = tween(durationMillis = 400))
//                            opsetY.animateTo(0f, animationSpec = tween(durationMillis = 400))
//                        }
//                    }

                    StoryItem(
                        imageUrl = data.photoUrl,
                        title = data.name,
                        content = data.description,
                        onClick = {},
                        id = data.id,
//                        modifier = Modifier
//                            .offset(y = opsetY.value.dp)
//                            .graphicsLayer(alpha = alpha.value)
                    )
                }
                item {
                    if(stories.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }





}