package com.example.storyapp.fake

import androidx.paging.PagingData
import com.example.storyapp.domain.GetAllStoryNewUseCase
import com.example.storyapp.domain.model.Story
import com.example.storyapp.utils.FakeFlowDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class FakeGetStoryNewUseCase : GetAllStoryNewUseCase {
    val fakeDelegate = FakeFlowDelegate<PagingData<Story>>()


    override suspend fun invoke(scope: CoroutineScope): Flow<PagingData<Story>> =  fakeDelegate.flow

}
