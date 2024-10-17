package com.example.storyapp.di

import android.content.Context
import androidx.room.Room
import com.example.storyapp.data.ApiService
import com.example.storyapp.data.local.room.RemoteKeysDao
import com.example.storyapp.data.local.room.StoryDao
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.remote.StoryRemoteMediator
import com.example.storyapp.datastore.TokenPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }

    @Provides
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao = storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): RemoteKeysDao =
        storyDatabase.remoteKeysDao()


}