package com.example.storyapp.presentation.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.dto.toLoginResult
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: UseCase,
): ViewModel() {

    private val _loginState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(loginData: LoginRequest) = viewModelScope.launch{
        Log.d("Login", "Email: ${loginData.email}, Password: ${loginData.password}")

        useCase.loginUseCase(loginData)
            .onStart {
                _loginState.value = ResultState.Loading
                Log.d("Login", "Loading")
            }
            .catch { e ->
                if(e.localizedMessage == "HTTP 401 Unauthorized"){
                    _loginState.value = ResultState.Error("Incorrect email or password")
                    Log.d("Login", "Error: Email atau password salah")
                    return@catch
                }
                _loginState.value = ResultState.Error("Something went wrong")
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

    private fun saveToken(token: String){
        viewModelScope.launch{
            Log.d("Proses Save Token", "Token: $token")
            useCase.saveUserTokenUseCase(token)
        }
    }

    fun resetState(){
        _loginState.value = ResultState.Idle
    }
}