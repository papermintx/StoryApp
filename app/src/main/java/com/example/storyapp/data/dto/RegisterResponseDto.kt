package com.example.storyapp.data.dto

import com.squareup.moshi.Json

data class RegisterResponseDto(

	@Json(name="error")
	val error: Boolean,

	@Json(name="message")
	val message: String
)
