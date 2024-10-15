package com.example.storyapp.presentation.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.dto.toStory
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.ResultState
import com.example.storyapp.domain.model.Story
import com.example.storyapp.domain.usecase.UseCase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val useCase: UseCase,
    private val tokenPreferencesRepository: TokenPreferencesRepository

) : ViewModel() {
    private val _listStoryWithLocation = MutableStateFlow<List<Story>>(emptyList())
    val listStoryWithLocation = _listStoryWithLocation.asStateFlow()

    init {
        fetchStories()
    }

    private fun fetchStories() = viewModelScope.launch {
        val tokenFlow = tokenPreferencesRepository.tokenPreferencesFlow.map {
            it.token
        }.firstOrNull()
        Log.d("MapViewModel", "Fetch Stories")
        tokenFlow?.let { token ->
            getAllStory("Bearer $token")
        }
        if (tokenFlow == null) {
            _listStoryWithLocation.value = emptyList()
        }
    }

    fun requestLocationUpdates(
        context : Context,
        locationCallback: (Location?) -> Unit
    ) {
        viewModelScope.launch {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    locationCallback(location)
                    Log.d("AddStoryViewModel", "requestLocationUpdates: $location")
                }.addOnFailureListener {
                    Log.e("AddStoryViewModel", "Failed to get location", it)
                    locationCallback(null)
                }
            } else {
                Log.e("AddStoryViewModel", "Permission denied")
                locationCallback(null)
            }
        }
    }

    private suspend fun getAllStory(bearerToken: String) {
        Log.d("MapViewModel", "Get All Story")

        useCase.getAllStoryUseCase(token = bearerToken, location = 1)
            .onStart {
                Log.d("Token bearer", bearerToken)
            }
            .catch { exception ->
                Log.d("HomeViewModel", exception.message.toString())
            }
            .collect { response ->
                val data = response.listStory.map { item -> item.toStory() }
                _listStoryWithLocation.value = data
            }
    }
}