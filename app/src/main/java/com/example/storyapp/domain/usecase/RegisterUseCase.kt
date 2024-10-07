package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.RegisterResponseDto
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: RemoteDataRepository
) {
    suspend operator fun invoke(registerData: RegisterRequest) : Flow<RegisterResponseDto> = flow {
        emit(repository.register(registerData))
    }
}