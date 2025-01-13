package com.example.samplecomposeapp.di

import com.example.samplecomposeapp.data.api.ApiService
import com.example.samplecomposeapp.data.api.RetrofitInstance
import com.example.samplecomposeapp.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiService(): ApiService = RetrofitInstance.api

    @Provides
    fun provideRepository(apiService: ApiService): Repository =
        Repository(apiService)
}