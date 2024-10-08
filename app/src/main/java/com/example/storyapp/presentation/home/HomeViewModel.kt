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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository
) : ViewModel() {

    private val _listStory = MutableStateFlow<ResultState<List<Story>>>(ResultState.Idle)
    val listStory = _listStory.asStateFlow()



    init {
        fetchStories()
        Log.d("HomeViewModel", "Init")
    }

     fun fetchStories() = viewModelScope.launch {
        val tokenFlow = tokenPreferencesRepository.tokenPreferencesFlow.map {
            it.token
        }.firstOrNull()
        Log.d("HomeViewModel", "Fetch Stories")
        tokenFlow?.let { token ->
            getAllStory("Bearer $token")
        }
        if (tokenFlow == null) {
            _listStory.value = ResultState.Error("Token not found")
        }
    }

    private suspend fun getAllStory(bearerToken: String) {
        Log.d("HomeViewModel", "Get All Story")

        _listStory.value = ResultState.Loading

        useCase.getAllStoryUseCase(token = bearerToken)
            .onStart {
                Log.d("Token bearer", bearerToken)
            }
            .catch { exception ->
                Log.d("HomeViewModel", exception.message.toString())
                _listStory.value = ResultState.Error("Something went wrong while fetching data")
            }
            .collect { response ->
                val data = response.listStory.map { item -> item.toStory() }
                _listStory.value = ResultState.Success(data)
            }
    }

    fun logout() = viewModelScope.launch {
        tokenPreferencesRepository.clearAuthToken()
    }

    fun resetState() {
        _listStory.value = ResultState.Idle
    }
}
