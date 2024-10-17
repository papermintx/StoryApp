package com.example.storyapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSourceFactory
import com.example.storyapp.data.ApiService
import com.example.storyapp.data.local.entity.StoryEntity
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.remote.StoryRemoteMediator
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.usecase.AddStoryUseCase
import com.example.storyapp.domain.usecase.GetAllStoryUseCase
import com.example.storyapp.domain.usecase.GetDetailStoryUseCase
import com.example.storyapp.domain.usecase.LoginUseCase
import com.example.storyapp.domain.usecase.RegisterUseCase
import com.example.storyapp.domain.usecase.UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideUsecase(
        loginUseCase: LoginUseCase,
        registerUseCase: RegisterUseCase,
        addStoryUseCase: AddStoryUseCase,
        getAllStoryUseCase: GetAllStoryUseCase,
        getDetailStoryUseCase: GetDetailStoryUseCase,
    ): UseCase{
        return UseCase(
            loginUseCase,
            registerUseCase,
            addStoryUseCase,
            getAllStoryUseCase,
            getDetailStoryUseCase,
        )
    }

    @Singleton
    @Provides
    fun provideTokenPreferencesRepository(
        @ApplicationContext context: Context
    ): TokenPreferencesRepository {
        return TokenPreferencesRepository(context)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideStoryPager(database: StoryDatabase, apiService: ApiService,  tokenPreferences: TokenPreferencesRepository): Pager<Int, StoryEntity>{
        return Pager(
            config = PagingConfig(
                pageSize = 8
            ),
            remoteMediator = StoryRemoteMediator(
                tokenPreferences = tokenPreferences,
                database = database,
                apiService = apiService
            ),
            pagingSourceFactory = PagingSourceFactory {
                database.storyDao().getAllStory()
            }
        )
    }


}