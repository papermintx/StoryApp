package com.example.storyapp.presentation.maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storyapp.domain.model.Story
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.*

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapsViewModel: MapViewModel = hiltViewModel(),
    mapType: MapType
) {
    val stories by mapsViewModel.listStoryWithLocation.collectAsState()

    val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.7893, 113.9213), 4f)

    val cameraPositionState = rememberCameraPositionState {
        position = cameraPosition
    }
    val clusteredStories = clusterMarkers(stories)


    var showDialog by remember { mutableStateOf(false) }

    var selectedCluster by remember { mutableStateOf<List<Story>?>(null) }


    // Menampilkan peta
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier,
        properties = MapProperties(
            mapType = mapType,
        )
    ) {
        // Mengelompokkan marker
        clusteredStories.forEach { cluster ->
            if (cluster.size > 1) {
                // Jika cluster memiliki lebih dari satu Story, tampilkan cluster marker dengan tanda lingkaran
                val firstStory = cluster[0]
                val lat = firstStory.lat
                val lon = firstStory.lon

                if (lat != null && lon != null) {
                    Marker(
                        state = MarkerState(position = LatLng(lat, lon)),
                        title = "Cluster of ${cluster.size} stories",
                        snippet = "Tap to view details",
                        onClick = {
                            selectedCluster = cluster
                            showDialog = true
                            true
                        }
                    )
                    // Tambahkan lingkaran untuk menandai cluster
                    Circle(
                        center = LatLng(lat, lon),
                        radius = 100.0, // Radius dalam meter (100 meter)
                        strokeColor = Color.Green.copy(alpha = 0.5f),
                        fillColor = Color.Green.copy(alpha = 0.2f)
                    )

                }
            } else {
                // Jika cluster hanya memiliki satu Story, tampilkan marker biasa
                val story = cluster[0]
                val lat = story.lat
                val lon = story.lon

                if (lat != null && lon != null) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(lat, lon)
                        ),
                        title = story.name,
                        snippet = story.description
                    )
                }
            }
        }
        if (showDialog && selectedCluster != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Cluster Details") },
                text = {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(selectedCluster!!) { story ->
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = "Sender: ${story.name}")
                                Text(text = "Content: ${story.description}")
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}// Fungsi untuk menghitung jarak menggunakan rumus Haversine
fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371000.0 // Radius bumi dalam meter

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}

// Mengelompokkan marker dengan radius yang lebih kecil untuk jarak dekat (100 meter)
fun clusterMarkers(stories: List<Story>, radius: Double = 100.0): List<List<Story>> {
    val clusters = mutableListOf<List<Story>>()
    val visited = mutableSetOf<Story>()

    for (story in stories) {
        if (story in visited) continue
        val cluster = mutableListOf<Story>()
        for (other in stories) {
            if (story != other && !visited.contains(other)) {
                val distance = calculateDistance(story.lat!!, story.lon!!, other.lat!!, other.lon!!)
                if (distance < radius) {
                    cluster.add(other)
                    visited.add(other)
                }
            }
        }
        cluster.add(story)
        visited.add(story)
        clusters.add(cluster)
    }
    return clusters
}
