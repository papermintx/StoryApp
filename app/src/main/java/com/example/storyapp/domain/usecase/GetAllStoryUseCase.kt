package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.GetStoryResponseDto
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllStoryUseCase @Inject constructor(
    private val repository: RemoteDataRepository
) {
    suspend operator fun invoke(token: String) : Flow<GetStoryResponseDto> = flow {
        emit(repository.getAllStories(token))
    }
}