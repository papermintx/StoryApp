package com.example.storyapp.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.storyapp.utils.PreferencesKeys
import com.example.storyapp.utils.TokenPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class TokenPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
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
        }
    }

    // clear token from datastore
    suspend fun clearAuthToken(){
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AUTH_TOKEN)
        }
    }
}