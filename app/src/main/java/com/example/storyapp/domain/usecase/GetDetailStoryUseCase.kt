package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.GetDetailStoryResponseDto
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetailStoryUseCase @Inject constructor(
    private val repository: RemoteDataRepository
) {

    suspend operator fun invoke(id: String) : Flow<GetDetailStoryResponseDto> = flow {
        emit(repository.getStoryById(id))
    }
}