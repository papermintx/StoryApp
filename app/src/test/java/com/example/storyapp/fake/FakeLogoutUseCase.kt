package com.example.storyapp.fake

import androidx.recyclerview.widget.DiffUtil
import com.example.storyapp.domain.LogoutUseCase
import com.example.storyapp.domain.model.Story

class FakeLogoutUseCase : LogoutUseCase {
    var isLoggedOut = false

    override suspend fun invoke() {
        isLoggedOut = true
    }
}

val diffCallback = object : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem.id == newItem.id
    }
}
