package com.example.storyapp.presentation.add.story

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.storyapp.R

@Composable
fun AddStoryScreen(modifier: Modifier = Modifier) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var inputText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Display selected image or placeholder
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop // Ensure the image is cropped correctly
            )
        } else {
            // Placeholder image
            Image(
                painter = painterResource(R.drawable.imagefolder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
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
                .fillMaxWidth() // Fill full width
                .height(250.dp) // Set TextField height
                .padding(vertical = 8.dp), // Vertical padding
            maxLines = 10, // Max lines
            placeholder = { Text("Start typing your story...") } // Placeholder
        )
    }
}