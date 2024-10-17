package com.example.storyapp.data.dto

import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.domain.model.Story
import com.squareup.moshi.Json

data class GetStoryResponseDto(

	@Json(name="listStory")
	val listStory: List<ListStoryItem>,

	@Json(name="error")
	val error: Boolean,

	@Json(name="message")
	val message: String
)
data class ListStoryItem(
	@Json(name = "photoUrl") val photoUrl: String,
	@Json(name = "createdAt") val createdAt: String,
	@Json(name = "name") val name: String,
	@Json(name = "description") val description: String,
	@Json(name = "lon") val lon: Double?, // Nullable
	@Json(name = "id") val id: String,
	@Json(name = "lat") val lat: Double? // Nullable
)