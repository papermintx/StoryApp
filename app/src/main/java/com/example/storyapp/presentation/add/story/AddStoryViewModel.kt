package com.example.storyapp.presentation.add.story

import androidx.lifecycle.ViewModel
import com.example.storyapp.domain.usecase.UseCase
import javax.inject.Inject

class AddStoryViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {
}