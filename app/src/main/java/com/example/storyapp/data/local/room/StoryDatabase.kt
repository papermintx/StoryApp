package com.example.storyapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyapp.data.local.entity.RemoteKeysEntity
import com.example.storyapp.data.local.entity.StoryEntity

@Database(entities = [StoryEntity::class, RemoteKeysEntity::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase()
{
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}