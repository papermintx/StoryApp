package com.example.storyapp.presentation.add.story

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.dto.toAddStoryResult
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.AddStoryResult
import com.example.storyapp.domain.usecase.UseCase
import com.example.storyapp.utils.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository
) : ViewModel() {

    private val _responseUploadStory = MutableStateFlow<ResultState<AddStoryResult>>(ResultState.Idle)
    val responseUploadStory = _responseUploadStory.asStateFlow()

    private fun uriToFile(uri: Uri, contentResolver: ContentResolver): File? {
        val file = File.createTempFile("image_", ".jpg", null)
        Log.d("AddStoryViewModel", "uriToFile one: $file")
        contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        Log.d("AddStoryViewModel", "uriToFile: $file")
        return file
    }

    fun uploadStory(uri: Uri, description: String, lat: Float? = null, lon: Float? = null, contentResolver: ContentResolver) {
        Log.d("AddStoryViewModel", "uploadStory: $uri")
        viewModelScope.launch {
            _responseUploadStory.value = ResultState.Loading
            try {
                val tokenPreferences = tokenPreferencesRepository.tokenPreferencesFlow.first()
                val token = tokenPreferences.token

                if (!token.isNullOrEmpty()) {
                    val file = uriToFile(uri, contentResolver)
                    if (file != null) {
                        val compressedFile = file.reduceFileImage()
                        Log.d("AddStoryViewModel", "uploadStory final compressed: ${compressedFile.length()} bytes")

                        if (compressedFile.length() > 1 * 1024 * 1024) {
                            Log.e("AddStoryViewModel", "Compressed file size exceeds 1 MB")
                            _responseUploadStory.value = ResultState.Error("Compressed file size exceeds 1 MB")
                            return@launch
                        }

                        val requestFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("photo", compressedFile.name, requestFile)
                        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

                        val tokenBearer = "Bearer $token"
                        Log.d("AddStoryViewModel", "uploadStory: $tokenBearer")

                        useCase.addStoryUseCase(
                            token = tokenBearer,
                            description = descriptionBody,
                            photo = body,
                            lat = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull()),
                            lon = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
                        )
                            .collect { response ->
                                Log.d("AddStoryViewModel", "uploadStory collect: $response")
                                if (response.error) {
                                    _responseUploadStory.value = ResultState.Error(response.message)
                                } else {
                                    val data = response.toAddStoryResult()
                                    _responseUploadStory.value = ResultState.Success(data)
                                }
                            }
                    }
                } else {
                    Log.e("AddStoryViewModel", "Token is null or empty")
                    _responseUploadStory.value = ResultState.Error("Token is null or empty")
                }
            } catch (e: HttpException) {
                Log.e("AddStoryViewModel", e.message())
                _responseUploadStory.value = ResultState.Error("HTTP error occurred")
            } catch (e: Exception) {
                Log.e("AddStoryViewModel", e.message.toString())
                _responseUploadStory.value = ResultState.Error("An error occurred")
            }
        }
    }

    fun resetState() {
        _responseUploadStory.value = ResultState.Idle
    }

}
