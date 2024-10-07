package com.example.storyapp.domain.usecase

import com.example.storyapp.data.dto.LoginResponseDto
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: RemoteDataRepository
) {
    suspend operator fun invoke(loginData: LoginRequest) : Flow<LoginResponseDto> = flow {
        emit(repository.login(loginData))
    }
}