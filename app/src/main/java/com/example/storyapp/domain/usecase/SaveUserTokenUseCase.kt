package com.example.storyapp.domain.usecase

import com.example.storyapp.datastore.TokenPreferencesRepository
import javax.inject.Inject

class SaveUserTokenUseCase @Inject constructor(
    private val tokenPreferencesRepository: TokenPreferencesRepository
) {

    suspend operator fun invoke(token: String) {
        tokenPreferencesRepository.saveAuthToken(token)
    }
}