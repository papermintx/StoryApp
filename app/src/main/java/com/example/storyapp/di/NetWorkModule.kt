package com.example.storyapp.di

import com.example.storyapp.data.ApiService
import com.example.storyapp.data.repository.RemoteDataRepositoryImpl
import com.example.storyapp.domain.repository.RemoteDataRepository
import com.example.storyapp.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Atur level logging
        }
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Menambahkan interceptor ke OkHttpClient
            .build()
    }

    // Provides Moshi instance
    @Provides
    @Singleton
    fun provideMoshit(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Provides Retrofit instance
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    // Provides Repository instance
    @Provides
    @Singleton
    fun provideRemoteDataRepository(apiService: ApiService): RemoteDataRepository {
        return RemoteDataRepositoryImpl(apiService)
    }

}