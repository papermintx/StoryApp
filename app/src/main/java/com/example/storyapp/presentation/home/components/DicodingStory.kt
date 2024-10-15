package com.example.storyapp.presentation.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.storyapp.domain.model.Story
import kotlinx.coroutines.delay

@Composable
fun DicodingStory(modifier: Modifier = Modifier, listStory: List<Story>, onStoryClick: (String) -> Unit) {
    val listState = rememberLazyListState()

    val firstItemVisible = remember { derivedStateOf { listState.firstVisibleItemIndex } }


    LazyColumn(
        modifier = modifier
            .fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(listStory){index, data ->
            val alpha = remember {
                Animatable(0f)
            }

            val opsetY = remember {
                Animatable(20f)
            }

            LaunchedEffect(firstItemVisible) {
                if (listState.firstVisibleItemIndex <= index) {
                    delay((index - listState.firstVisibleItemIndex) * 90L)
                    alpha.animateTo(1f, animationSpec = tween(durationMillis = 400))
                    opsetY.animateTo(0f, animationSpec = tween(durationMillis = 400))
                }
            }


            StoryItem(
                imageUrl = data.photoUrl,
                title = data.name,
                content = data.description,
                onClick = onStoryClick,
                id = data.id,
                modifier = Modifier
                    .offset(y = opsetY.value.dp)
                    .graphicsLayer(alpha = alpha.value)
            )
        }
    }


}