package com.example.storyapp.domain.model

data class LoginResult(
    val name: String,
    val userId: String,
    val token: String
)