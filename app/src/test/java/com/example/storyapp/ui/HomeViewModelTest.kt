package com.example.storyapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.storyapp.domain.model.Story
import com.example.storyapp.fake.FakeGetStoryNewUseCase
import com.example.storyapp.fake.FakeLogoutUseCase
import com.example.storyapp.fake.diffCallback
import com.example.storyapp.presentation.home.HomeViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.StoryPagingSource
import com.example.storyapp.utils.noopListUpdateCallback
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private val getStoriesUseCase = FakeGetStoryNewUseCase()
    private val getLogoutUseCase = FakeLogoutUseCase()



    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(getLogoutUseCase, getStoriesUseCase)
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyData = DataDummy.generateDummyStory()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyData)

        getStoriesUseCase.fakeDelegate.emit(data)

        val differ = AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        val job = launch {
            homeViewModel.stories.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()
        job.cancel()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyData.size, differ.snapshot().size)
        Assert.assertEquals(dummyData.first(), differ.snapshot().firstOrNull())
    }


    @Test
    fun `when Get Story Should Return Zero Data When No Stories Available`() = runTest {
        val emptyData: PagingData<Story> = StoryPagingSource.snapshot(emptyList())
        getStoriesUseCase.fakeDelegate.emit(emptyData)

        val differ = AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        val job = launch {
            homeViewModel.stories.collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()
        job.cancel()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(0, differ.snapshot().size)
    }


    @Test
    fun `when Logout Should Change Logout State`() = runTest {
        homeViewModel.logout()

        advanceUntilIdle()

        assertTrue(getLogoutUseCase.isLoggedOut)
    }



}



