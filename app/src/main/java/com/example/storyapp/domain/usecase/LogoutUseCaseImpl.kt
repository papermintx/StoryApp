package com.example.storyapp.domain.usecase

import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.LogoutUseCase

class LogoutUseCaseImpl(
    private val tokenPreferencesRepository: TokenPreferencesRepository
): LogoutUseCase {

    override suspend operator fun invoke() {
        tokenPreferencesRepository.clearAuthToken()
    }
}