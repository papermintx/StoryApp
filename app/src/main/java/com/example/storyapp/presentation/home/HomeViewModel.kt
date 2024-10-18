package com.example.storyapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.data.mapper.toStory
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    pager: Pager<Int, StoryEntity>,
    private val tokenPreferencesRepository: TokenPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val uiState = _uiState.asStateFlow()

    val storyPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { data ->
                data.toStory()
            }
        }
        .cachedIn(viewModelScope)

    fun setUiState(state: ResultState<Boolean>) {
        _uiState.value = state
    }

    fun resetState() {
        _uiState.value = ResultState.Idle
    }

    fun logout() {
        viewModelScope.launch {
            tokenPreferencesRepository.clearAuthToken()
        }
    }

}