package com.example.storyapp.domain.usecase

data class UseCase (
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val addStoryUseCase: AddStoryUseCase,
    val getAllStoryUseCase: GetAllStoryUseCase,
    val getDetailStoryUseCase: GetDetailStoryUseCase,
    val getAllStoryWithLocationUseCase: GetAllStoryWithLocationUseCase
)