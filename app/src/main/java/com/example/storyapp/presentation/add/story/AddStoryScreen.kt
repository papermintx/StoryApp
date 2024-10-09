package com.example.storyapp.presentation.add.story
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.storyapp.R
import com.example.storyapp.domain.ResultState
import com.example.storyapp.presentation.components.DialogError
import com.example.storyapp.presentation.components.LoadingDialog
import com.example.storyapp.presentation.navigation.NavScreen


@Composable
fun AddStoryScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModel: AddStoryViewModel = hiltViewModel()
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var inputText by remember { mutableStateOf("") }

    val data = viewModel.responseUploadStory.collectAsState()

    LaunchedEffect(Unit) {
        navHostController.currentBackStackEntry?.savedStateHandle?.getLiveData<Uri?>("photoUri")?.observeForever { uri ->
            selectedImageUri = uri
        }
    }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // Enable scrolling
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
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
                onClick = {
                    navHostController.navigate(NavScreen.Camera.route)
                }
            ) {
                Text(text = "Camera")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    launcher.launch("image/*")
                }
            ) {
                Text(text = "Gallery")
            }
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
                if(selectedImageUri != null && inputText.isNotBlank()) {
                    viewModel.uploadStory(selectedImageUri!!, description = inputText, contentResolver = context.contentResolver)
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
                navHostController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                viewModel.resetState()
                navHostController.popBackStack()
            }
            is ResultState.Error -> {
                DialogError(onDismiss = {
                    viewModel.resetState()
                   navHostController.popBackStack()
                }, message = result.exception)
            }
            is ResultState.Loading -> {
                LoadingDialog()
            }
            else -> Unit
        }
    }
}
