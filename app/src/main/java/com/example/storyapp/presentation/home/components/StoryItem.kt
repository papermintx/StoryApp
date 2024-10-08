package com.example.storyapp.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.storyapp.utils.fonts.MyTypography

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    id: String,
    imageUrl: String,
    title: String,
    content: String,
    onClick: (String) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RectangleShape)
            .clickable(
                onClick = {
                    onClick(id)
                }
            ),
        shape = RectangleShape,

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp) // Padding internal Card
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true) // Animasi transisi gambar
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    ,
                contentScale = ContentScale.Crop // Mempertahankan aspek rasio
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MyTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black, // Warna teks judul
                maxLines = 1, // Batas satu baris
                overflow = TextOverflow.Ellipsis // Potong teks jika terlalu panjang
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = content,
                style = MyTypography.bodyLarge,
                color = Color.Gray, // Warna teks konten
                maxLines = 2, // Batas dua baris
                overflow = TextOverflow.Ellipsis // Potong teks jika terlalu panjang
            )
        }
    }
}
