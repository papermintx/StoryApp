package com.example.storyapp.presentation.detail

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
class DetailStoryViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository
): ViewModel() {
    private val _storyDetail = MutableStateFlow<ResultState<Story>>(ResultState.Idle)
    val storyDetail = _storyDetail.asStateFlow()

    fun getDetailStory(id: String) = viewModelScope.launch {
        val token = tokenPreferencesRepository.tokenPreferencesFlow
            .map { it.token }
            .firstOrNull()

        if (token != null) {
            val bearerToken = "Bearer $token"
            useCase.getDetailStoryUseCase(bearerToken, id)
                .onStart {
                    _storyDetail.value = ResultState.Loading
                }
                .catch { exception ->
                    Log.d("DetailStoryViewModel", exception.message.toString())
                    _storyDetail.value = ResultState.Error("Something went wrong while fetching data")
                }
                .collect { response ->
                    if (response.error) {
                        Log.d("DetailStoryViewModel", response.message)
                        _storyDetail.value = ResultState.Error("Something went wrong while fetching data")
                        return@collect
                    }

                    Log.d("DetailStoryViewModel", response.story.toString())
                    val data = response.story.toStory()
                    _storyDetail.value = ResultState.Success(data)
                }
        } else {
            _storyDetail.value = ResultState.Error("Token is null")
        }
    }

}
