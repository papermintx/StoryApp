package com.example.storyapp.presentation.add.story
import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.storyapp.R
import com.example.storyapp.domain.ResultState
import com.example.storyapp.presentation.camera.RequestPermission
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.navigation.NavScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddStoryScreen(
    modifier: Modifier = Modifier,
    viewModel: AddStoryViewModel = hiltViewModel(),
    imageUri: Uri?,
    currentBackStack:() -> Unit,
    goCamera:() -> Unit,
    goGallery:() -> Unit,
    goBack:() -> Unit,
    backHandler:() -> Unit
) {
    var inputText by remember { mutableStateOf("") }


    var location by remember{
        mutableStateOf<LatLng?>(null)
    }

    val data = viewModel.responseUploadStory.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var isLocationEnabled by remember { mutableStateOf(false) }


    BackHandler {
        showDialog = true
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm") },
            text = { Text("Are you sure you want to discard your story?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    goBack()
                    backHandler()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    val permissionState = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ))

    if (isLocationEnabled && !permissionState.allPermissionsGranted) {
        LaunchedEffect(isLocationEnabled) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(isLocationEnabled) {
        if (isLocationEnabled && permissionState.allPermissionsGranted) {
            viewModel.requestLocationUpdates(context) { loc ->
                loc?.let {
                    location = LatLng(it.latitude, it.longitude)
                    Toast.makeText(context, "Your location has been successfully obtained", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        currentBackStack()
        location = null
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .height(400.dp),
                contentScale = ContentScale.Crop // Ensure the image is cropped correctly
            )
        } else {
            Image(
                painter = painterResource(R.drawable.imagefolder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = goCamera
            ) {
                Text(text = "Camera")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = goGallery
            ) {
                Text(text = "Gallery")
            }
        }
        // Switch untuk mengizinkan lokasi
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enable Location")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isLocationEnabled,
                onCheckedChange = { isLocationEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for input
        TextField(
            value = inputText,
            onValueChange = { newText -> inputText = newText },
            label = { Text("Enter your story") },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp),
            maxLines = 10,
            placeholder = { Text("Start typing your story...") } // Placeholder
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button untuk upload story
        Button(
            onClick = {
                if(imageUri != null && inputText.isNotBlank()) {
                    viewModel.uploadStory(imageUri!!, description = inputText, contentResolver = context.contentResolver, latLng = location)
                } else{
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "Upload Story")
        }


        when (val result = data.value) {
            is ResultState.Success -> {
                Toast.makeText(context, "Story uploaded successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                goBack
            }
            is ResultState.Error -> {
                DialogError(onDismiss = {
                    viewModel.resetState()
                    goBack
                }, message = result.exception)
            }
            is ResultState.Loading -> {
                LoadingDialog()
            }
            else -> Unit
        }
    }
}
