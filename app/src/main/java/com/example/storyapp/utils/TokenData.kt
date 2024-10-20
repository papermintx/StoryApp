package com.example.storyapp.utils

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val TOKEN_PREFERENCES_NAME = "token_preferences"
val Context.tokenDataStore by preferencesDataStore(name = TOKEN_PREFERENCES_NAME)

data class TokenPreferences(
    val token: String?
)

object PreferencesKeys {
    val AUTH_TOKEN = stringPreferencesKey("auth_token")
}
