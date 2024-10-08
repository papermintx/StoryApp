package com.example.storyapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.storyapp.domain.model.Story

@Composable
fun DicodingStory(modifier: Modifier = Modifier, listStory: List<Story>, onStoryClick: (String) -> Unit) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(listStory.size) { index ->
            val data = listStory[index]
            StoryItem(imageUrl = data.photoUrl, title = data.name, content = data.description, onClick = onStoryClick, id = data.id)
        }
    }


}