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

) : ViewModel() {
    private val _listStoryWithLocation = MutableStateFlow<List<Story>>(emptyList())
    val listStoryWithLocation = _listStoryWithLocation.asStateFlow()

    init {
        fetchStories()
    }

    private fun fetchStories() = viewModelScope.launch {
        useCase.getAllStoryUseCase.invoke(location = 1)
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