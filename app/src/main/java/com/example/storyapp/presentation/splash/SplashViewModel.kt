package com.example.storyapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.datastore.TokenPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenPreferencesRepository: TokenPreferencesRepository
) : ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val tokenNotNull = _token.asStateFlow()

    init {
        viewModelScope.launch {
            tokenPreferencesRepository.tokenPreferencesFlow.map {
                it.token
            }.collect { token ->
                _token.value = token
            }
        }
    }
}
