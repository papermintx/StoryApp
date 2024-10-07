package com.example.storyapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.dto.toStory
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.Story
import com.example.storyapp.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository
): ViewModel(){
    private val _listStory = MutableStateFlow<ResultState<List<Story>>>(ResultState.Idle)
    val listStory = _listStory.asStateFlow()

    val token = tokenPreferencesRepository.tokenPreferencesFlow.map {
        it.token
    }

    init {
        getAllStory()
    }

    private fun getAllStory() = viewModelScope.launch {
        Log.d("HomeViewModel", "Get All Story")

        // Collect the token value from the Flow
        val currentToken = tokenPreferencesRepository.tokenPreferencesFlow
            .map { it.token } // Extract the token
            .firstOrNull()    // Get the first value or null if not present

        if (currentToken != null) {
            val bearerToken = "Bearer $currentToken" // Add Bearer prefix to the token

            useCase.getAllStoryUseCase(token = bearerToken)
                .onStart {
                    Log.d("Token bearer", bearerToken)
                    _listStory.value = ResultState.Loading
                }
                .catch { exception ->
                    Log.d("HomeViewModel", exception.message.toString())
                    _listStory.value = ResultState.Error("Something went wrong while fetching data")
                }
                .collect { response ->
                    val data = response.listStory.map { item ->
                        item.toStory()
                    }
                    _listStory.value = ResultState.Success(data)
                }
        } else {
            // Handle case when token is null
            Log.d("HomeViewModel", "Token is null")
            _listStory.value = ResultState.Error("Token is not available")
        }
    }

}