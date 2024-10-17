package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.GetStoryResponseDto
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllStoryUseCase @Inject constructor(
    private val repository: RemoteDataRepository
) {
    suspend operator fun invoke(token: String, page: Int? = null, size: Int? = null, location: Int? = 0): Flow<GetStoryResponseDto> = flow {
        emit(repository.getAllStories(token, page, size, location))
    }
}