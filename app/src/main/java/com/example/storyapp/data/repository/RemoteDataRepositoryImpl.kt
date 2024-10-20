package com.example.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.data.ApiService
import com.example.storyapp.data.dto.AddStoryResponseDto
import com.example.storyapp.data.dto.GetDetailStoryResponseDto
import com.example.storyapp.data.dto.GetStoryResponseDto
import com.example.storyapp.data.dto.LoginResponseDto
import com.example.storyapp.data.dto.RegisterResponseDto
import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.remote.StoryRemoteMediator
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class RemoteDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
): RemoteDataRepository {

    override suspend fun register(registerData: RegisterRequest): RegisterResponseDto {
        return withContext(Dispatchers.Default){
            apiService.registerUser(registerData)
        }
    }

    override suspend fun login(loginData: LoginRequest): LoginResponseDto {
        return withContext(Dispatchers.Default){
            apiService.loginUser(loginData.email, loginData.password)
        }
    }

    override suspend fun addStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponseDto {

        return withContext(Dispatchers.Default){
            apiService.addStory(description, photo, lat, lon)
        }
    }

    override suspend fun addStoryGuest(
        description: String,
        photo: String,
        lat: String?,
        lon: String?
    ): AddStoryResponseDto {
        val descriptionBody =  description.toRequestBody("text/plain".toMediaTypeOrNull())
        val file = File(photo)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photoBody = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val latBody = lat?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lonBody = lon?.toRequestBody("text/plain".toMediaTypeOrNull())

        return withContext(Dispatchers.Default){
            apiService.addStoryGuest(descriptionBody, photoBody, latBody, lonBody)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getAllStories(
        page: Int?,
        size: Int?,
        location: Int?
    ): GetStoryResponseDto {
        return withContext(Dispatchers.Default){
            apiService.getAllStories(
                page = page,
                size = size,
                location = location
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getAllStoriesNew(): Pager<Int, StoryEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
            ),
            remoteMediator = StoryRemoteMediator(
                database = storyDatabase,
                apiService = apiService,
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        )
    }

    override suspend fun getStoryById(
        id: String
    ): GetDetailStoryResponseDto {
        return withContext(Dispatchers.Default){
            apiService.getStoryDetail(id)
        }
    }
}