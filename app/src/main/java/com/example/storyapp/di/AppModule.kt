package com.example.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.storyapp.datastore.TokenPreferencesRepository
import com.example.storyapp.domain.GetAllStoryNewUseCase
import com.example.storyapp.domain.LogoutUseCase
import com.example.storyapp.domain.repository.RemoteDataRepository
import com.example.storyapp.domain.usecase.AddStoryUseCase
import com.example.storyapp.domain.usecase.GetAllStoryNewUseCaseImpl
import com.example.storyapp.domain.usecase.GetAllStoryUseCase
import com.example.storyapp.domain.usecase.GetDetailStoryUseCase
import com.example.storyapp.domain.usecase.LoginUseCase
import com.example.storyapp.domain.usecase.LogoutUseCaseImpl
import com.example.storyapp.domain.usecase.RegisterUseCase
import com.example.storyapp.domain.usecase.SaveUserTokenUseCase
import com.example.storyapp.domain.usecase.UseCase
import com.example.storyapp.utils.tokenDataStore
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
        saveUserTokenUseCase: SaveUserTokenUseCase
    ): UseCase{
        return UseCase(
            loginUseCase,
            registerUseCase,
            addStoryUseCase,
            getAllStoryUseCase,
            getDetailStoryUseCase,
            saveUserTokenUseCase
        )
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        tokenPreferencesRepository: TokenPreferencesRepository
    ): LogoutUseCase {
        return LogoutUseCaseImpl(tokenPreferencesRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllStoryNewUseCase(
        remoteDataRepository: RemoteDataRepository
    ): GetAllStoryNewUseCase {
        return GetAllStoryNewUseCaseImpl(remoteDataRepository)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.tokenDataStore
    }

    @Singleton
    @Provides
    fun provideTokenPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): TokenPreferencesRepository {
        return TokenPreferencesRepository(dataStore)
    }

}