package com.example.storyapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.storyapp.domain.GetAllStoryNewUseCase
import com.example.storyapp.domain.LogoutUseCase
import com.example.storyapp.domain.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getAllStoryNewUseCase: GetAllStoryNewUseCase
) : ViewModel() {

    // MutableStateFlow untuk menyimpan PagingData
    private val _stories = MutableStateFlow<PagingData<Story>>(PagingData.empty())
    val stories: StateFlow<PagingData<Story>> get() = _stories

    init {
        fetchStories()
    }

    private fun fetchStories() {
        viewModelScope.launch {
            getAllStoryNewUseCase(viewModelScope).collect { pagingData ->
                _stories.value = pagingData
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

}