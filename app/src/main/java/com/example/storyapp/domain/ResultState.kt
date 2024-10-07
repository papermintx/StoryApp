package com.example.storyapp.domain

sealed interface ResultState<out T> {
    data object Idle: ResultState<Nothing>
    data object Loading: ResultState<Nothing>
    data class Success<T>(val data: T): ResultState<T>
    data class Error(val exception: String): ResultState<Nothing>
    data class Empty(val message: String): ResultState<Nothing>
}