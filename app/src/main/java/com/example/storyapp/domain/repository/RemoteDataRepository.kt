package com.example.storyapp.domain.repository

import com.example.storyapp.data.dto.AddStoryResponseDto
import com.example.storyapp.data.dto.GetDetailStoryResponseDto
import com.example.storyapp.data.dto.GetStoryResponseDto
import com.example.storyapp.data.dto.LoginResponseDto
import com.example.storyapp.data.dto.RegisterResponseDto
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.domain.model.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RemoteDataRepository {

    // Register a new user
    suspend fun register(registerData: RegisterRequest): RegisterResponseDto

    // Login a user
    suspend fun login(loginData: LoginRequest): LoginResponseDto

    // Add a story
    suspend fun addStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponseDto

    // Add a story as a guest
    suspend fun addStoryGuest(description: String, photo: String, lat: String?, lon: String?): AddStoryResponseDto

    // Get all stories
    suspend fun getAllStories(token: String, page: Int? = null, size: Int? = null, location: Int? = 0): GetStoryResponseDto

    // Get a story by id
    suspend fun getStoryById(token: String, id: String): GetDetailStoryResponseDto
}