package com.example.storyapp.domain

import androidx.paging.PagingData
import com.example.storyapp.domain.model.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface GetAllStoryNewUseCase {

    suspend operator fun invoke(scope: CoroutineScope):  Flow<PagingData<Story>>
}