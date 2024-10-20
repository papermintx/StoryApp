package com.example.storyapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.storyapp.data.mapper.toStory
import com.example.storyapp.domain.GetAllStoryNewUseCase
import com.example.storyapp.domain.model.Story
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllStoryNewUseCaseImpl(
    private val remoteDataRepository: RemoteDataRepository
): GetAllStoryNewUseCase {
    override suspend operator fun invoke(scope: CoroutineScope): Flow<PagingData<Story>> {
        return remoteDataRepository.getAllStoriesNew().flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }.cachedIn(scope)
    }
}