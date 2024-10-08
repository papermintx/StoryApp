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

data class UserPreferences(
    val isDark: Boolean?
)

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.userDataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

class UserPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
){
    private val dataStore = context.userDataStore

    private object PreferencesKeys{
        val USER_PREFERENCES = stringPreferencesKey("user_preferences")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch{ exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map{ preferences ->
            val isDark = preferences[PreferencesKeys.USER_PREFERENCES]
            UserPreferences(isDark?.toBoolean())
        }

    // save data to datastore
    suspend fun saveAuthToken(isDark: Boolean){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_PREFERENCES] = isDark.toString()
            Log.d("UserPreferences", "UserPreferences saved Succes: $isDark")
        }
    }

    // clear token from datastore
    suspend fun clearAuthToken(){
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_PREFERENCES)
        }
    }
}