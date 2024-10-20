package com.example.storyapp.utils

import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.domain.model.Story

object DataDummy {
    fun generateDummyStory(): List<Story> {
        val stories = ArrayList<Story>()

        for (i in 0 until 10) {
            val story = Story(
                id = i.toString(),
                name = "Story $i",
                description = "Description $i",
                photoUrl = "https://picsum.photos/200/300?random=$i",
                createdAt = "2021-08-01",
                lon = i.toDouble(),
                lat = i.toDouble(),
            )
            stories.add(story)
        }

        return stories
    }

    fun generateDummyStoryEntity(): List<StoryEntity> {
        val stories = ArrayList<StoryEntity>()

        for (i in 0 until 10) {
            val story = StoryEntity(
                id = i.toString(),
                name = "Story $i",
                description = "Description $i",
                photoUrl = "https://picsum.photos/200/300?random=$i",
                createdAt = "2021-08-01",
                lon = i.toDouble(),
                lat = i.toDouble(),
            )
            stories.add(story)
        }

        return stories
    }
}