package com.example.storyapp.presentation.maps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.mapper.toStory
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.model.Story
import com.example.storyapp.domain.usecase.UseCase
import com.example.storyapp.utils.generateBearerToken
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
class MapViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository

) : ViewModel() {
    private val _listStoryWithLocation = MutableStateFlow<List<Story>>(emptyList())
    val listStoryWithLocation = _listStoryWithLocation.asStateFlow()

    init {
        fetchStories()
    }

    private fun fetchStories() = viewModelScope.launch {
        val tokenFlow = tokenPreferencesRepository.tokenPreferencesFlow.map {
            it.token
        }.firstOrNull()
        Log.d("MapViewModel", "Fetch Stories")
        tokenFlow?.let { token ->
            getAllStoryWithLocation(token)
        }
        if (tokenFlow == null) {
            _listStoryWithLocation.value = emptyList()
        }
    }

    private suspend fun getAllStoryWithLocation(bearerToken: String) {
        Log.d("MapViewModel", "Get All Story")
        val token = generateBearerToken(bearerToken)

        useCase.getAllStoryUseCase.invoke(token = token, location = 1)
            .onStart {
                Log.d("MapViewModel", "Start")
            }
            .catch {
                Log.e("MapViewModel", "Error", it)
            }
            .collect { response ->
                Log.d("MapViewModel", "Collect")
                val data = response.listStory.map {
                    it.toStory()
                }
                _listStoryWithLocation.value = data
            }
    }
}