package com.example.storyapp.data.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.storyapp.data.ApiService
import com.example.storyapp.data.dto.AddStoryResponseDto
import com.example.storyapp.data.dto.GetDetailStoryResponseDto
import com.example.storyapp.data.dto.GetStoryResponseDto
import com.example.storyapp.data.dto.LoginResponseDto
import com.example.storyapp.data.dto.RegisterResponseDto
import com.example.storyapp.domain.model.LoginRequest
import com.example.storyapp.domain.model.RegisterRequest
import com.example.storyapp.domain.repository.RemoteDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class RemoteDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
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
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponseDto {

        return withContext(Dispatchers.Default){
            apiService.addStory(token,  description, photo, lat, lon)
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

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): GetStoryResponseDto {
        return withContext(Dispatchers.Default){
            apiService.getAllStories(token, page, size, location)
        }
    }

    override suspend fun getStoryById(
        token: String,
        id: String
    ): GetDetailStoryResponseDto {
        return withContext(Dispatchers.Default){
            apiService.getStoryDetail(token, id)
        }
    }
}