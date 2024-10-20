package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.AddStoryResponseDto
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddStoryUseCase @Inject constructor(
    private val repository: RemoteDataRepository
){
    suspend operator fun invoke(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<AddStoryResponseDto> = flow {
        emit(repository.addStory(description, photo, lat, lon))
    }
}