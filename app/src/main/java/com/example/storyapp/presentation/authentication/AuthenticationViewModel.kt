package com.example.storyapp.presentation.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.dto.toLoginResult
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository
): ViewModel() {

    private val _loginState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val registerState = _registerState.asStateFlow()

    fun login(loginData: LoginRequest) = viewModelScope.launch{
        Log.d("Login", "Email: ${loginData.email}, Password: ${loginData.password}")

        useCase.loginUseCase(loginData)
            .onStart {
                _loginState.value = ResultState.Loading
                Log.d("Login", "Loading")
            }
            .catch { e ->
                _loginState.value = ResultState.Error("Error: ${e.localizedMessage}")
                Log.d("Login", "Error: ${e.localizedMessage}")
            }
            .collect { responseDto ->
                if (responseDto.error) {
                    _loginState.value = ResultState.Success(false)
                    Log.d("Login", "Error: ${responseDto.message}")
                    return@collect
                }

                val response = responseDto.loginResult.toLoginResult()
                saveToken(response.token)

                _loginState.value = ResultState.Success(true)
                Log.d("Login", "Success")
                Log.d("Login", "Token: ${response.token}")
            }
    }


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

    private fun saveToken(token: String){
        viewModelScope.launch{
            Log.d("Proses Save Token", "Token: $token")
            tokenPreferencesRepository.saveAuthToken(token)
        }
    }

    fun resetState(){
        _loginState.value = ResultState.Idle
        _registerState.value = ResultState.Idle
    }

}