package com.example.storyapp.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


data class TokenPreferences(
    val token: String?
)

private const val TOKEN_PREFERENCES_NAME = "token_preferences"
private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_PREFERENCES_NAME)

class TokenPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
){
    private val dataStore = context.tokenDataStore

    private object PreferencesKeys{
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    val tokenPreferencesFlow: Flow<TokenPreferences> = dataStore.data
        .catch{ exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map{ preferences ->
            val authToken = preferences[PreferencesKeys.AUTH_TOKEN]
            TokenPreferences(authToken)
        }

    // save token to datastore
    suspend fun saveAuthToken(token: String){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN] = token
            Log.d("Token", "Token saved Succes: $token")
        }
    }

    // clear token from datastore
    suspend fun clearAuthToken(){
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AUTH_TOKEN)
        }
    }
}