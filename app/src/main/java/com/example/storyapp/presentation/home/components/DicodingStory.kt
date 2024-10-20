package com.example.storyapp.presentation.home.components

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.storyapp.domain.model.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DicodingStory(
    modifier: Modifier = Modifier, stories: LazyPagingItems<Story>,
    isRefresh: Boolean,
    resetRefresh: () -> Unit,
    gotoDetail: (String) -> Unit
) {
    val isRefreshing = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val listState = rememberLazyListState()

    val firstItemVisible = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()

    // Trigger refresh when isRefresh is true
    LaunchedEffect(isRefresh) {
        if (isRefresh) {
            onRefresh(coroutineScope, stories, isRefreshing, resetRefresh)
        }
    }

    LaunchedEffect(stories.loadState) {
        if (stories.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error something went wrong",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = state,
            isRefreshing = isRefreshing.value,
            onRefresh = {
                onRefresh(coroutineScope, stories, isRefreshing, resetRefresh)

            },
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(stories.itemCount) { index ->
                    val data = stories[index] ?: return@items

//                    val alpha = remember {
//                        Animatable(0f)
//                    }

                    val opsetY = remember {
                        Animatable(20f)
                    }

                    LaunchedEffect(firstItemVisible) {
                        if (listState.firstVisibleItemIndex <= index) {
                            delay((index - listState.firstVisibleItemIndex) * 90L)
//                            alpha.animateTo(1f, animationSpec = tween(durationMillis = 400))
                            opsetY.animateTo(0f, animationSpec = tween(durationMillis = 400))
                        }
                    }

                    StoryItem(
                        imageUrl = data.photoUrl,
                        title = data.name,
                        content = data.description,
                        onClick = gotoDetail,
                        id = data.id,
                        modifier = Modifier
                            .offset(y = opsetY.value.dp)
//                            .graphicsLayer(alpha = alpha.value)
                    )
                }
                item {
                    if (stories.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

private fun onRefresh(
    coroutineScope: CoroutineScope,
    stories: LazyPagingItems<Story>,
    isRefreshing: MutableState<Boolean>,
    resetRefresh: () -> Unit
) {
    isRefreshing.value = true
    coroutineScope.launch {
        stories.refresh()
        delay(2000)
        isRefreshing.value = false
        resetRefresh()
    }
}