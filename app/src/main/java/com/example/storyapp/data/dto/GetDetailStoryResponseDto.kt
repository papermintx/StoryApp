package com.example.storyapp.data.dto

import com.squareup.moshi.Json

data class GetDetailStoryResponseDto(

	@Json(name="error")
	val error: Boolean,

	@Json(name="message")
	val message: String,

	@Json(name="story")
	val story: StoryDto
)

data class StoryDto(

	@Json(name="photoUrl")
	val photoUrl: String,

	@Json(name="createdAt")
	val createdAt: String,

	@Json(name="name")
	val name: String,

	@Json(name="description")
	val description: String,

	@Json(name="lon")
	val lon: Any?,

	@Json(name="id")
	val id: String,

	@Json(name="lat")
	val lat: Any?
)

