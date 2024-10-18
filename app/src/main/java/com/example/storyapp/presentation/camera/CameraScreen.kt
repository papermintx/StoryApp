package com.example.storyapp.presentation.camera

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storyapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    activity: Activity,
    goBack: () -> Unit,
    currentBackstackEntry: (Uri) -> Unit,
) {

    val controller = remember {
        LifecycleCameraController(
            activity.applicationContext
        ).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    // Menyimpan status izin untuk kamera
    val cameraPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.CAMERA
        )
    )

    // Meminta izin kamera saat komponen ditampilkan
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.allPermissionsGranted) {
            cameraPermissionState.launchMultiplePermissionRequest()
        }
    }


    val cameraViewModel : CameraViewModel = hiltViewModel()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            currentBackstackEntry(it)
            goBack()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .size(45.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        galleryLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.baseline_photo_library_24), contentDescription = null)
            }

            Spacer(modifier = Modifier.width(1.dp))

            // Tombol untuk mengambil foto
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        cameraViewModel.onTakePhoto(controller = controller) { uri ->
                            uri?.let { data ->
                                currentBackstackEntry(data)
                                Toast.makeText(activity, data.toString(), Toast.LENGTH_SHORT).show()
                            }
                           goBack()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.baseline_photo_camera_24), contentDescription = "Take Photo")
            }

            Spacer(modifier = Modifier.width(1.dp))

            // Tombol untuk mengganti kamera
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .size(45.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                    },
                contentAlignment = Alignment.Center
            ) {
               Icon(painter = painterResource(R.drawable.baseline_cameraswitch_24), contentDescription = null)
            }
        }
    }
}




