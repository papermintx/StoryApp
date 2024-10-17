package com.example.storyapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.data.mapper.toStory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    pager: Pager<Int, StoryEntity>
) : ViewModel() {

    val storyPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { data ->
                data.toStory()
            }
        }
        .cachedIn(viewModelScope)

}
