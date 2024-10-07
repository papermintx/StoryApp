package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.AddStoryResponseDto
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddStoryUseCase @Inject constructor(
    private val repository: RemoteDataRepository
){
    suspend operator fun invoke(token: String, description: String, photo: String, lat: String?, lon: String?): Flow<AddStoryResponseDto> = flow {
        emit(repository.addStory(token, description, photo, lat, lon))
    }
}