package com.example.storyapp.data.dto

import com.example.storyapp.domain.model.LoginResult
import com.squareup.moshi.Json

data class LoginResponseDto(

	@Json(name="loginResult")
	val loginResult: LoginResultDto,

	@Json(name="error")
	val error: Boolean,

	@Json(name="message")
	val message: String
)

data class LoginResultDto(

	@Json(name="name")
	val name: String,

	@Json(name="userId")
	val userId: String,

	@Json(name="token")
	val token: String
)

fun LoginResultDto.toLoginResult(): LoginResult{
	return LoginResult(
		name = name,
		userId = userId,
		token = token
	)
}
