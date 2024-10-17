package com.example.storyapp.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.data.ApiService
import com.example.storyapp.data.local.entity.RemoteKeysEntity
import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.mapper.toEntity
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.utils.generateBearerToken
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val tokenPreferences: TokenPreferencesRepository
): RemoteMediator<Int, StoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult  {
        return try {
            val loadPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextkey?.minus(1) ?: STARTING_PAGE_INDEX
                    Log.d("StoryRemoteMediator", "LoadType.REFRESH")
                }
                LoadType.PREPEND -> {
                    val remotekeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remotekeys?.prevkey ?: return MediatorResult.Success(endOfPaginationReached = remotekeys != null)
                    Log.d("StoryRemoteMediator", "LoadType.PREPEND")
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    val nextKey = remoteKeys?.nextkey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    Log.d("StoryRemoteMediator", "LoadType.APPEND")
                    nextKey
                }
            }

            val token = tokenPreferences.tokenPreferencesFlow.first().token
            token?.let { tokenData ->
                val response = apiService.getAllStories(token = generateBearerToken(tokenData), page = loadPage, size = state.config.pageSize)
                val endOfPaginationReached = response.listStory.isEmpty()
                val stories = response.listStory.map { story ->
                    story.toEntity()
                }

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.storyDao().deleteAllStory()
                        database.remoteKeysDao().deleteRemoteKeys()
                    }
                    val prevKey = if (loadPage == 1) null else loadPage - 1
                    val nextKey = if (endOfPaginationReached) null else loadPage + 1
                    val keys = stories.map {
                        RemoteKeysEntity(id = it.id, prevkey = prevKey, nextkey = nextKey)
                    }
                    database.remoteKeysDao().insertAll(keys)
                    database.storyDao().insertStory(stories)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
            return MediatorResult.Error(Exception("Token is null"))
        } catch (e : Exception) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val STARTING_PAGE_INDEX = 1
    }

}