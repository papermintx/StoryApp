package com.example.storyapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.storyapp.data.dto.toStory
import com.example.storyapp.data.local.entity.StoryEntity
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

    private val _stories = MutableStateFlow<PagingData<StoryEntity>>(PagingData.empty())
    val stories: StateFlow<PagingData<StoryEntity>> = _stories


    init {
//        fetchStories()
        // Launch a coroutine to collect the paging data
        viewModelScope.launch {
            useCase.getAllStoryUseCase().collect { pagingData ->
                _stories.value = pagingData
            }
        }
        Log.d("HomeViewModel", "Init")
    }

//     fun fetchStories() = viewModelScope.launch {
//        val tokenFlow = tokenPreferencesRepository.tokenPreferencesFlow.map {
//            it.token
//        }.firstOrNull()
//        Log.d("HomeViewModel", "Fetch Stories")
//        tokenFlow?.let { token ->
//            getAllStory("Bearer $token")
//        }
//        if (tokenFlow == null) {
//            _listStory.value = ResultState.Error("Token not found")
//        }
//    }

//    private suspend fun getAllStory(bearerToken: String) {
//        Log.d("HomeViewModel", "Get All Story")
//
//        _listStory.value = ResultState.Loading
//
//        useCase.getAllStoryUseCase()
//            .onStart {
//                Log.d("Token bearer", bearerToken)
//            }
//            .catch { exception ->
//                Log.d("HomeViewModel", exception.message.toString())
//                _listStory.value = ResultState.Error("Something went wrong while fetching data")
//            }
//            .collect { response ->
//                Log.d("HomeViewModel", response.toString())
//                _listStory.value = ResultState.Success(response.map { it.toStory() })
//            }
//    }

    fun logout() = viewModelScope.launch {
        tokenPreferencesRepository.clearAuthToken()
    }

    fun resetState() {
        _listStory.value = ResultState.Idle
    }
}
