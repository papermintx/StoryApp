package com.example.storyapp.domain.repository

import android.net.Uri
import androidx.camera.view.LifecycleCameraController

interface CameraRepository {
    suspend fun takePhoto(controller : LifecycleCameraController): Uri
}