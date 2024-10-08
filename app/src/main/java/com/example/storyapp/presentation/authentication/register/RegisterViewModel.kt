package com.example.storyapp.presentation.authentication.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: UseCase,
): ViewModel() {

    private val _registerState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val registerState = _registerState.asStateFlow()


    fun register(registerData: RegisterRequest) = viewModelScope.launch{
        useCase.registerUseCase(registerData)
            .onStart {
                _registerState.value = ResultState.Loading
                Log.d("Register", "Loading")
            }
            .catch { e ->
                _registerState.value = ResultState.Error("Error: ${e.localizedMessage}")
                Log.d("Register", "Error: ${e.localizedMessage}")
            }
            .collect { responseDto ->
                if (responseDto.error){
                    _registerState.value = ResultState.Success(false)
                    Log.d("Register", "Error: ${responseDto.message}")
                    return@collect
                }
                _registerState.value = ResultState.Success(true)
                Log.d("Register", "Success")
            }
    }

    fun resetState(){
        _registerState.value = ResultState.Idle
    }
}