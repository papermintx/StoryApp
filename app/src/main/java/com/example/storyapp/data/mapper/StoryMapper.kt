package com.example.storyapp.data.mapper

import com.example.storyapp.data.dto.ListStoryItem
import com.example.storyapp.data.dto.StoryDto
import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.domain.model.Story

fun StoryDto.toStory() = Story(
    photoUrl = photoUrl,
    createdAt = createdAt,
    name = name,
    description = description,
    lon = lon as Double?,
    id = id,
    lat = lat as Double?
)

fun StoryDto.toStoryEntity() = StoryEntity(
    photoUrl = photoUrl,
    createdAt = createdAt,
    name = name,
    description = description,
    lon = lon as Double?,
    id = id,
    lat = lat as Double?
)


fun StoryEntity.toStory() = Story(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
    photoUrl = photoUrl,
    lon = lon,
    lat = lat
)

fun ListStoryItem.toEntity() = StoryEntity(
    photoUrl = photoUrl,
    createdAt = createdAt,
    name = name,
    description = description,
    lon = lon ?: 0.0,
    id = id,
    lat = lat ?: 0.0
)


fun ListStoryItem.toStory() = Story(
    photoUrl = photoUrl,
    createdAt = createdAt,
    name = name,
    description = description,
    lon = lon,
    id = id,
    lat = lat
)
