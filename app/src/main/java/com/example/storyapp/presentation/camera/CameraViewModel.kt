    package com.example.storyapp.presentation.camera
    import android.net.Uri
    import androidx.camera.view.LifecycleCameraController
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.storyapp.domain.repository.CameraRepository
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.launch
    import javax.inject.Inject


    @HiltViewModel
    class CameraViewModel @Inject constructor(
        private val cameraRepository: CameraRepository
    ) : ViewModel() {

        fun onTakePhoto(
            controller: LifecycleCameraController,
            onPhotoTaken: (Uri?) -> Unit,
        ) {
            viewModelScope.launch {
                val uri = cameraRepository.takePhoto(controller)
                onPhotoTaken(uri)
            }
        }
    }
