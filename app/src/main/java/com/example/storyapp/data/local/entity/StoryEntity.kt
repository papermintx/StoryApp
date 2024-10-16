package com.example.storyapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.storyapp.domain.model.Story
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    val id: String,
    val name:String,
    val description:String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    val lon: Double,
    val lat: Double
) : Parcelable


fun StoryEntity.toStory() = Story(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
    photoUrl = photoUrl,
    lon = lon,
    lat = lat
)