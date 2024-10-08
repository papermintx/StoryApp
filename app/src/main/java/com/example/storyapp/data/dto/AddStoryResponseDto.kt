package com.example.storyapp.data.dto

import com.example.storyapp.domain.model.AddStoryResult
import com.squareup.moshi.Json

data class AddStoryResponseDto(

	@Json(name="error")
	val error: Boolean,

	@Json(name="message")
	val message: String
)

fun AddStoryResponseDto.toAddStoryResult(): AddStoryResult {
	return AddStoryResult(
		error = error,
		message = message
	)
}
